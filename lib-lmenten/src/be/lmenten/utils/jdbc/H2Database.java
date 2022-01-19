package be.lmenten.utils.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.h2.api.ErrorCode;

public abstract class H2Database
	implements AutoCloseable
{
	private static final String IFEXISTS = "IFEXISTS";

	private final String url;
	private final Properties properties;

	private Connection connection = null;

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * 
	 * @param url
	 * @param properties
	 */
	protected H2Database( String url, Properties properties )
	{
		this.url = Objects.requireNonNull( url );
		this.properties = Objects.requireNonNull( properties );
	}

	/**
	 * 
	 * @return
	 */
	public abstract int getDatabaseVersion();

	/**
	 * 
	 * @return
	 */
	public final Connection getConnection()
	{
		return connection;
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * 
	 * @throws SQLException
	 */
	public void open()
		throws SQLException
	{
		// --------------------------------------------------------------------
		// - We will honour user's IFEXISTS policy ----------------------------
		// --------------------------------------------------------------------

		boolean ifExists = false;

		boolean hasIfExists = properties.containsKey( IFEXISTS );
		if( hasIfExists )
		{
			ifExists = properties.get( IFEXISTS ).toString().equalsIgnoreCase( "TRUE" );
		}

		// --------------------------------------------------------------------
		// - Try opening database ---------------------------------------------
		// --------------------------------------------------------------------

		try
		{
			LOG.fine( "Trying to open database '" + url + "' ..." );

			properties.put( IFEXISTS, true );

			connection = DriverManager.getConnection( url, properties );

			int oldVersion = getStoredDatabaseVersion();
			if( oldVersion == -1 )
			{
				LOG.severe( "Could not retrieve stored database version." );
			}
			else
			{
				LOG.finer( "Stored database version : " + oldVersion );

				if( oldVersion < getDatabaseVersion() )
				{
					LOG.fine( "Upgrading from version " + oldVersion + " to version " + getDatabaseVersion() );

					privUpdate( oldVersion );
				}

				else if( oldVersion > getDatabaseVersion() )
				{
					LOG.fine( "Downgrading from version " + oldVersion + " to version " + getDatabaseVersion() + " not supported");

					throw new SQLException( "Downgrading not supported" );
				}
			}
		}

		catch( SQLException ex1 )
		{
			if( ex1.getErrorCode() == 90146 /*ErrorCode.DATABASE_NOT_FOUND_WITH_IF_EXISTS_1*/ )
			{
				LOG.fine( "Database does not exits." );

				if( hasIfExists && ifExists )
				{
					LOG.severe( "Failed to open database, honouring user IFEXISTS flag" );

					throw ex1;
				}

				try
				{
					LOG.fine( "Trying to create new database ..." );
				
					properties.remove( IFEXISTS );
					connection = DriverManager.getConnection( url, properties );

					privCreate();

					LOG.finer( "Database successfuly created." );
				}
				catch( SQLException ex2 )
				{
					LOG.log( Level.SEVERE, "Failed to create database", ex1 );
				}
			}
			else
			{
				LOG.log( Level.SEVERE, "Failed to open database", ex1 );

				throw ex1;
			}
		}

		LOG.finer( "Database successfuly opened." );
	}

	// ------------------------------------------------------------------------
	// - 
	// ------------------------------------------------------------------------

	private static final String PROPERTIES_TABLE_NAME = "properties";
	private static final String PROPERTIES_COLUMN_KEY = "property_key";
	private static final String PROPERTIES_COLUMN_VALUE = "property_value";

	private static final String SQL_PROPERTIES =
			"CREATE TABLE " + PROPERTIES_TABLE_NAME + " "
		+	"( "
		+		PROPERTIES_COLUMN_KEY + " VARCHAR PRIMARY KEY, "
		+		PROPERTIES_COLUMN_VALUE	+ " VARCHAR "
		+	")"
		;

	/**
	 * 
	 * @throws SQLException
	 */
	private void privCreate()
		throws SQLException
	{
		Statement stmt = connection.createStatement();
		stmt.executeUpdate( SQL_PROPERTIES );
		stmt.close();

		create();

		setStoredDatabaseVersion( getDatabaseVersion() );
	}

	/**
	 * 
	 * @param oldVersion
	 * @throws SQLException
	 */
	private void privUpdate( int oldVersion )
		throws SQLException
	{
		update( oldVersion );

		setStoredDatabaseVersion( getDatabaseVersion() );
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected int getStoredDatabaseVersion()
		throws SQLException
	{
		String versionRaw = getProperty( "database.version" );
		if( versionRaw == null )
		{
			return -1;
		}

		return Integer.parseInt( versionRaw );
	}

	/**
	 * 
	 * @param version
	 * @return
	 */
	protected boolean setStoredDatabaseVersion( int version )
	{
		return setProperty( "database.version", Integer.toString( version ) );
	}

	// ------------------------------------------------------------------------
	// - 
	// ------------------------------------------------------------------------

	protected abstract void create()
		throws SQLException;

	protected abstract void update( long oldVersion )
		throws SQLException;

	// ========================================================================
	// =
	// ========================================================================

	@Override
	public void close()
		throws SQLException
	{
		LOG.fine( "Closing database ..." );

		if( connection != null )
		{
			connection.close();
		}

		LOG.finer( "Database succesfully closed." );
	}

	// ========================================================================
	// = Properties service ===================================================
	// ========================================================================

	private static final String SQL_FETCH_PROPERTY =
			"SELECT " + PROPERTIES_COLUMN_VALUE + " "
		+	"FROM " + PROPERTIES_TABLE_NAME + " "
		+	"WHERE " + PROPERTIES_COLUMN_KEY + " = ?"
		;

	/**
	 * 
	 * @param _key
	 * @return
	 */
	public final String getProperty( String _key )
	{
		return getProperty( _key, null );
	}

	/**
	 * 
	 * @param _key
	 * @param defaultValue
	 * @return
	 */
	public final String getProperty( String _key, String defaultValue )
	{
		try( PreparedStatement stmt = connection.prepareStatement( SQL_FETCH_PROPERTY ) )
		{
			stmt.setString( 1, _key );

			ResultSet rs = stmt.executeQuery();
			if( rs.next() )
			{
				return rs.getString( 1 );
			}
		}
		catch( SQLException ex )
		{
			LOG.log( Level.SEVERE, "Failed to fetch property with key '" + _key + "'.", ex );
		}

		return defaultValue;
	}

	// ------------------------------------------------------------------------

	private static final String SQL_MERGE_PROPERTY =
			"MERGE INTO " + PROPERTIES_TABLE_NAME + " "
		+	"KEY( " + PROPERTIES_COLUMN_KEY + " ) "
		+	"VALUES( ?, ? )"
		;

	/**
	 * 
	 * @param _key
	 * @param _value
	 * @return
	 */
	public final boolean setProperty( String _key, String _value )
	{
		try( PreparedStatement stmt = connection.prepareStatement( SQL_MERGE_PROPERTY ) )
		{
			stmt.setString( 1, _key );
			stmt.setString( 2, _value );
			stmt.execute();

			return true;
		}
		catch( SQLException ex )
		{
			LOG.log( Level.SEVERE, "Failed to store property with key '" + _key + "' and value '" + _value + "'.", ex );
		}

		return false;
	}

	// ========================================================================
	// =
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( H2Database.class.getName() );
}
