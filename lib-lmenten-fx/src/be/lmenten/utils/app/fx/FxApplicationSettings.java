package be.lmenten.utils.app.fx;

import be.lmenten.utils.logging.LogLevel;
import be.lmenten.utils.logging.fx.LogWindow;
import javafx.beans.property.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class FxApplicationSettings
{
	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_DEBUG_MODE_ENABLED
		= "be.lmenten.debug";

	public static final boolean DEBUG_MODE_ENABLED_DEFAULT
		= false;

	private final BooleanProperty debugModeEnabled
		= new SimpleBooleanProperty( this, "debugModeEnabled" );

	public BooleanProperty debugModeEnabledProperty()
	{
		return debugModeEnabled;
	}

	public boolean isDebugModeEnabled()
	{
		return debugModeEnabled.get();
	}

	public void setDebugModeEnabled( boolean debugModeEnabled )
	{
		this.debugModeEnabled.set( debugModeEnabled );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_LOG_LEVEL
		= "be.lmenten.logLevel";

	public static final LogLevel LOG_LEVEL_DEFAULT
		= LogLevel.WARNING;

	private final ObjectProperty<LogLevel> logLevel
		= new SimpleObjectProperty<>( this, "logLevel" );

	public ObjectProperty<LogLevel> logLevelProperty()
	{
		return logLevel;
	}

	public LogLevel getLogLevel()
	{
		return logLevel.get();
	}

	public void setLogLevel( LogLevel logLevel )
	{
		this.logLevel.set( logLevel );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_LOG_FILTER
		= "be.lmenten.logFilter";

	public static final String LOG_FILTER_DEFAULT
		= "be.lmenten.*";

	private final StringProperty logFilter
		= new SimpleStringProperty( this, "logFilter" );

	public StringProperty getLogFilterProperty()
	{
		return logFilter;
	}

	public String getLogFilter()
	{
		return logFilter.get();
	}

	public void setLogFilter( String logFilter )
	{
		this.logFilter.set( logFilter );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_LOG_DIR
		= "be.lmenten.logDir";

	private static final String LOG_DIR_DEFAULT
		= "./logs";

	private final StringProperty logDir
			= new SimpleStringProperty( this, "logDir" );

	public StringProperty logDirProperty()
	{
		return logDir;
	}

	public String getLogDir()
	{
		return logDir.get();
	}

	public void setLogDir( String logDir )
	{
		this.logDir.set( logDir );
	}

	// ========================================================================
	// =
	// ========================================================================

	private static final String PROPERTY_LOG_FILE_ENABLED
		= "be.lmenten.logFileEnabled";

	private static final boolean LOG_FILE_ENABLED_DEFAULT
		= true;

	private final BooleanProperty logFileEnabled
		= new SimpleBooleanProperty( this, "logFileEnabled" );

	public BooleanProperty logFileEnabledProperty()
	{
		return logFileEnabled;
	}

	public boolean isLogFileEnabled()
	{
		return logFileEnabled.get();
	}

	public void setLogFileEnabled( boolean logFileEnabled )
	{
		this.logFileEnabled.set( logFileEnabled );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_KEEP_LOG_FILE_ENABLED
		= "be.lmenten.keepLogFile";

	public static final boolean KEEP_LOG_FILE_ENABLED_DEFAULT
		= true;

	private final BooleanProperty keepLogFileEnabled
		= new SimpleBooleanProperty( this, "keepLogFileEnabled" );

	public BooleanProperty keepLogFileEnabledProperty()
	{
		return keepLogFileEnabled;
	}

	public boolean isKeepLogFileEnabled()
	{
		return keepLogFileEnabled.get();
	}

	public void setKeepLogFileEnabled( boolean keepLogFileEnabled )
	{
		this.keepLogFileEnabled.set( keepLogFileEnabled );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_ANSI_LOG_OUTPUT_ENABLED
		= "be.lmenten.ansiLogOutput";

	public static final boolean ANSI_LOG_OUTPUT_ENABLED_DEFAULT
		= false;

	private final BooleanProperty ansiLogOutputEnabled
		= new SimpleBooleanProperty( this, "ansiLogOutputEnabled" );

	public BooleanProperty logAnsiOutputEnabledProperty()
	{
		return ansiLogOutputEnabled;
	}

	public boolean isAnsiLogOutputEnabled()
	{
		return ansiLogOutputEnabled.get();
	}

	public void setAnsiLogOutputEnabled( boolean ansiLogOutputEnabled )
	{
		this.ansiLogOutputEnabled.set( ansiLogOutputEnabled );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_SHOW_LOG_WINDOW_ENABLED
		= "be.lmenten.showLogWindow";

	public static final boolean SHOW_LOG_WINDOW_ENABLED_DEFAULT
		= false;

	private final BooleanProperty showLogWindowEnabled
		= new SimpleBooleanProperty( this, "showLogWindowEnabled" );

	public BooleanProperty showLogWindowEnabledProperty()
	{
		return showLogWindowEnabled;
	}

	public boolean isShowLogWindowEnabled()
	{
		return showLogWindowEnabled.get();
	}

	public void setShowLogWindowEnabled( boolean showLogWindowEnabled )
	{
		this.showLogWindowEnabled.set( showLogWindowEnabled );
	}

	// ========================================================================
	// =
	// ========================================================================

	public static final String PROPERTY_SHOW_EXCEPTION_WINDOW_ENABLED
		= "be.lmenten.showExceptionWindow";

	public static final boolean SHOW_EXCEPTION_WINDOW_ENABLED_DEFAULT
		= true;

	private final BooleanProperty showExceptionWindowEnabled
			= new SimpleBooleanProperty( this, "showExceptionWindowEnabled" );

	public void setShowExceptionWindowEnabled( boolean showExceptionWindowEnabled )
	{
		this.showExceptionWindowEnabled.set( showExceptionWindowEnabled );
	}

	public boolean isShowExceptionWindowEnabled()
	{
		return showExceptionWindowEnabled.get();
	}

	public BooleanProperty showExceptionWindowEnabledProperty()
	{
		return showExceptionWindowEnabled;
	}

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	private final FxApplication app;

	/**
	 *
	 * @param app the application
	 */
	/*package*/ FxApplicationSettings( FxApplication app )
	{
		this.app = app;

		// --------------------------------------------------------------------
		// - Load the preferences and override the values with those from
		// - system properties.
		// --------------------------------------------------------------------

		loadFromPreferences();
		loadFromSystemProperties();

		// --------------------------------------------------------------------
		// - Set default behaviours -------------------------------------------
		// --------------------------------------------------------------------

		showLogWindowEnabled.addListener( ( property, oldValue, newValue ) ->
		{
			if( newValue == true )
			{
				LogWindow.display();
			}
			else
			{
				LogWindow.hide();
			}
		} );
	}

	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Load the settings for the application from the preferences store.
	 * <p>
	 * This method also sets initial default values if the setting is not
	 * found in preferences store.
	 */
	private void loadFromPreferences()
	{
		Preferences prefs = Preferences.userNodeForPackage( app.getClass() );

		setDebugModeEnabled( prefs.getBoolean( PROPERTY_DEBUG_MODE_ENABLED, DEBUG_MODE_ENABLED_DEFAULT ) );

		setLogLevel( LogLevel.parse( prefs.get( PROPERTY_LOG_LEVEL, LOG_LEVEL_DEFAULT.name() ) ) );
		setLogFilter( prefs.get( PROPERTY_LOG_FILTER, LOG_FILTER_DEFAULT ) );

		setLogDir( prefs.get( PROPERTY_LOG_DIR, LOG_DIR_DEFAULT ) );
		setLogFileEnabled( prefs.getBoolean( PROPERTY_LOG_FILE_ENABLED, LOG_FILE_ENABLED_DEFAULT ) );
		setKeepLogFileEnabled( prefs.getBoolean( PROPERTY_KEEP_LOG_FILE_ENABLED, KEEP_LOG_FILE_ENABLED_DEFAULT ) );

		setAnsiLogOutputEnabled( prefs.getBoolean( PROPERTY_ANSI_LOG_OUTPUT_ENABLED, ANSI_LOG_OUTPUT_ENABLED_DEFAULT ) );
		setShowLogWindowEnabled( prefs.getBoolean( PROPERTY_SHOW_LOG_WINDOW_ENABLED, SHOW_LOG_WINDOW_ENABLED_DEFAULT ) );
		setShowExceptionWindowEnabled( prefs.getBoolean( PROPERTY_SHOW_EXCEPTION_WINDOW_ENABLED, SHOW_EXCEPTION_WINDOW_ENABLED_DEFAULT ) );
	}

	/**
	 * <p>
	 * Save the settings to the preferences store.
	 * <p>
	 * NOTE: This will store the overridden values.
	 */
	public void saveToPreferences()
	{
		Preferences prefs = Preferences.userNodeForPackage( app.getClass() );

		try
		{
			prefs.putBoolean( PROPERTY_DEBUG_MODE_ENABLED, isDebugModeEnabled() );

			prefs.put( PROPERTY_LOG_LEVEL, getLogLevel().name() );
			prefs.put( PROPERTY_LOG_FILTER, getLogFilter() );

			prefs.put( PROPERTY_LOG_DIR, getLogDir() );
			prefs.putBoolean( PROPERTY_LOG_FILE_ENABLED, isLogFileEnabled() );
			prefs.putBoolean( PROPERTY_KEEP_LOG_FILE_ENABLED, isKeepLogFileEnabled() );

			prefs.putBoolean( PROPERTY_ANSI_LOG_OUTPUT_ENABLED, isAnsiLogOutputEnabled() );
			prefs.putBoolean( PROPERTY_SHOW_LOG_WINDOW_ENABLED, isShowLogWindowEnabled() );
			prefs.putBoolean( PROPERTY_SHOW_EXCEPTION_WINDOW_ENABLED, isShowExceptionWindowEnabled() );

			prefs.flush();
		}
		catch( Exception ex )
		{
			LOG.log( Level.SEVERE, "", ex );
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Load the settings from the system properties (i.e. set with -D from
	 * the command line).
	 */
	private void loadFromSystemProperties()
	{
		String s = System.getProperty( PROPERTY_DEBUG_MODE_ENABLED );
		if( s != null )
		{
			debugModeEnabled.set( Boolean.parseBoolean( s ) );
		}

		s = System.getProperty( PROPERTY_ANSI_LOG_OUTPUT_ENABLED );
		if( s != null )
		{
			ansiLogOutputEnabled.set( Boolean.parseBoolean( s ) );
		}

		s = System.getProperty( PROPERTY_LOG_LEVEL );
		if( s != null )
		{
			logLevel.set( LogLevel.parse( s ) );
		}

		s = System.getProperty( PROPERTY_LOG_FILTER );
		if( s != null )
		{
			logFilter.set( s );
		}

		s = System.getProperty( PROPERTY_SHOW_LOG_WINDOW_ENABLED );
		if( s != null )
		{
			showLogWindowEnabled.set( Boolean.parseBoolean( s ) );
		}
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( FxApplicationSettings.class.getName() );
}
