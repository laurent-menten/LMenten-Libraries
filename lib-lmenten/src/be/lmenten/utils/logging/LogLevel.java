package be.lmenten.utils.logging;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * <p>
 * Drop-in replacement for java.util.logging.Level, used with beans to allow
 * easy property edition in PropertySheets etc.
 *
 * <p>
 * Also supports user friendly localized name through toString().
 */
public enum LogLevel
{
	OFF			( Level.OFF ),

	SEVERE		( Level.SEVERE ),
	WARNING		( Level.WARNING ),
	INFO		( Level.INFO ),
	CONFIG		( Level.CONFIG ),
	FINE		( Level.FINE ),
	FINER		( Level.FINER),
	FINEST		( Level.FINEST ),

	ALL			( Level.ALL )
	;

	// ------------------------------------------------------------------------

	private final Level level;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	LogLevel( Level level )
	{
		this.level = level;
	}

	// ========================================================================
	// =
	// ========================================================================

	public Level getLevel()
	{
		return level;
	}

	public static LogLevel valueOf( Level level )
	{
		return LogLevel.valueOf( level.getName() );
	}

	// ========================================================================
	// = Level interface mock =================================================
	// ========================================================================

	public String getLocalizedName()
	{
		return level.getLocalizedName();
	}

	public String getName()
	{
		return level.getName();
	}

	public int intValue()
	{
		return level.intValue();
	}

	public static LogLevel parse( String name )
	{
		return LogLevel.valueOf( name );
	}

	// ========================================================================
	// = Object interface =====================================================
	// ========================================================================

	@Override
	public String toString()
	{
		return RES.getString( this.name() );
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	@SuppressWarnings( "unused" )
	private static final Logger LOG
		= Logger.getLogger( LogLevel.class.getName() );

	@SuppressWarnings( "unused" )
	private static final Preferences PREFS
		= Preferences.userNodeForPackage( LogLevel.class );

	/*package*/ static final ResourceBundle RES
		= ResourceBundle.getBundle( LogLevel.class.getName() );
}
