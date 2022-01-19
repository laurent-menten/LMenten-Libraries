package be.lmenten.utils.app.fx;

import be.lmenten.utils.logging.AnsiLogFormatter;
import be.lmenten.utils.logging.LogFormatter;
import be.lmenten.utils.logging.LogLevel;
import be.lmenten.utils.logging.LogRegExPackageFilter;
import be.lmenten.utils.logging.fx.LogWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.PropertyKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.lang.Runtime.Version;

/**
 * An abstract JavaFx application base class with various capabilities like:
 * <ul>
 *     <li>Debug</li>
 *     <li>Logging</li>
 * </ul>
 *
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2022 / 01 / 02
 */
public abstract class FxApplication
	extends Application
	implements FxApplicationConstants
{
	// ------------------------------------------------------------------------
	// - Runtime constants with defaults --------------------------------------
	// ------------------------------------------------------------------------

	/*package*/ boolean debugMode
		= Boolean.parseBoolean( System.getProperty( PROPERTY_DEBUG, "false" ) );

	/*package*/ boolean logAnsiOutput
		= Boolean.parseBoolean( System.getProperty( PROPERTY_LOG_ANSI_OUTPUT, "false" ) );

	/*package*/ LogLevel logLevel
		= LogLevel.parse( System.getProperty( PROPERTY_LOG_LEVEL, "OFF" ) );

	/*package*/ boolean disableLogFilter
		= Boolean.parseBoolean( System.getProperty( PROPERTY_DISABLE_LOG_FILTER, "false" ) );

	/*package*/ String logFilter
			= System.getProperty( PROPERTY_LOG_FILTER,"be.lmenten.*" );

	/*package*/ boolean disableLogfile
			= Boolean.parseBoolean( System.getProperty( PROPERTY_LOG_DISABLE_LOGFILE, "false" ) );

	/*package*/ boolean keepLogfile
		= Boolean.parseBoolean( System.getProperty( PROPERTY_LOG_KEEP_LOGFILE, "false" ) );

	/*package*/ boolean showLogWindow
		= Boolean.parseBoolean( System.getProperty( PROPERTY_DEBUG_SHOW_LOG_WINDOW, "false" ) );

	/*package*/ boolean showExceptionWindow
		= Boolean.parseBoolean( System.getProperty( PROPERTY_DEBUG_SHOW_EXCEPTION_WINDOW, "false" ) );

	public final boolean runningFromExe
		= Boolean.parseBoolean( System.getProperty( PROPERTY_LAUNCH4J_FLAG, "false" ) );

	// ------------------------------------------------------------------------
	// - Runtime variables ----------------------------------------------------
	// ------------------------------------------------------------------------

	private final FxApplicationSettings settings;

	private final Preferences APP_PREFS;

	private boolean logFileOK;
	private String logDirname;
	private String logFilename = null;

	protected Stage stage;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	protected FxApplication()
	{
		settings = new FxApplicationSettings( this );

		// --------------------------------------------------------------------
		// - Custom log output formatter --------------------------------------
		// --------------------------------------------------------------------

		if( logAnsiOutput )
		{
			AnsiLogFormatter.install( logLevel );
		}
		else
		{
			LogFormatter.install( logLevel );
		}

		LOG.fine( "Starting application " + getClass().getSimpleName() );

		LOG.finer( "Console log formatter installed." );

		// --------------------------------------------------------------------
		// - Preferences ------------------------------------------------------
		// --------------------------------------------------------------------

		APP_PREFS = Preferences.userRoot().node( getClass().getName() );

		logDirname = APP_PREFS.get( PREFS_LOGGING_DIRECTORY, "./logs" );
	}

	// ========================================================================
	// =
	// ========================================================================

	public abstract String getAppTitle();
	public abstract Version getAppVersion();
	public abstract long getBuildNumber();
	public abstract LocalDateTime getBuildDateTime();

	public FxApplicationSettings getSettings()
	{
		return settings;
	}

	// ========================================================================
	// = Fx initialization ====================================================
	// ========================================================================

	/**
	 * Call this first in application init() method.
	 *
	 * @throws Exception
	 */
	@Override
	public void init()
		throws Exception
	{
		LOG.fine( "FxApplication.init() ..." );

		// --------------------------------------------------------------------
		// - Log file ---------------------------------------------------------
		// --------------------------------------------------------------------

		logFileOK = false;

		if( ! disableLogfile )
		{
			File logDir = new File( logDirname );
			if( ! logDir.exists() )
			{
				LOG.finer( "Log folder \"" + logDirname + "\" does not exists." );

				if( ! logDir.mkdirs() )
				{
					LOG.warning( "Could not create log folder." );
				}
				else
				{
					LOG.finer( "Log folder successfully created." );
				}
			}

			// ----------------------------------------------------------------

			if( logDir.exists() )
			{
				LOG.finer( "Installing log file handler." );

				String logFileNamePrefix
					= logDir + File.separator + getClass().getSimpleName();

				logFilename = LogFormatter.logToFile( logFileNamePrefix );
				if( logFileOK = (logFilename == null) )
				{
					LOG.warning("Could not instal log file handle.");
				}
			}
			else
			{
				LOG.severe( "No log folder -> no log file." );
			}
		}

		// --------------------------------------------------------------------
		// - Filter out foreign packages from logs ----------------------------
		// --------------------------------------------------------------------

		if( ! disableLogFilter )
		{
			LOG.finer( "Installing log source filter" );

			LogRegExPackageFilter.install( logFilter, logLevel );
		}
	}

	// ========================================================================
	// = Fx start =============================================================
	// ========================================================================

	/**
	 * Call this first in application start(stage) method.
	 *
	 * @param stage
	 * @throws Exception
	 */
	@Override
	public void start( Stage stage )
		throws Exception
	{
		LOG.fine( "FxApplication.start() ..." );

		this.stage = stage;

		// --------------------------------------------------------------------
		// - First open log window i needed ----------------------------------
		// --------------------------------------------------------------------

		if( debugMode || showLogWindow )
		{
			LogWindow.display();
		}

		logRuntimeInfos();

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------

		stage.setOnCloseRequest( ev ->
		{
			this.closeRequest( ev );
		} );
	}

	// ========================================================================
	// = Fx cleanup ===========================================================
	// ========================================================================

	/**
	 * Call this last in application stop() method.
	 *
	 * @throws Exception
	 */
	@Override
	public void stop()
		throws Exception
	{
		LOG.fine( "FxApplication.stop() ..." );

		// ------------------------------------------------------------
		// - If not in debug mode remove log file ---------------------
		// ------------------------------------------------------------

		if( ! disableLogfile )
		{
			if( logFileOK )
			{
				if( !debugMode && !keepLogfile )
				{
					LOG.finer("Deleting log file \"" + logFilename + "\"");

					Path logfilePath = Paths.get(logFilename);

					try {
						if( Files.exists( logfilePath ) )
						{
							Files.delete( logfilePath );
						}
					}
					catch( IOException ex )
					{
						LOG.log( Level.WARNING, "Could not delete logfile.", ex );
					}
				}
				else
				{
					LOG.finer("Keeping log file \"" + logFilename + "\"" );
				}
			}
		}

		// ------------------------------------------------------------
		// - Final action : close log window --------------------------
		// ------------------------------------------------------------

		LogWindow.close();
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 *
	 * @param ev
	 */
	protected void closeRequest( WindowEvent ev )
	{
		ev.consume();

		// --------------------------------------------------------------------
		// - Confirmation dialog ----------------------------------------------
		// --------------------------------------------------------------------

		Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
		alert.initOwner( stage );
		alert.initModality( Modality.APPLICATION_MODAL );

		alert.setTitle( getAppTitle() );
		alert.setHeaderText( RES.getString( "alert.quit.header" ) );
		alert.setContentText( RES.getString( "alert.quit.content.nosave" ) );

		ButtonType yesButton = new ButtonType( RES.getString("yes"), ButtonBar.ButtonData.YES );
		ButtonType cancelButton = new ButtonType( RES.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE );
		alert.getButtonTypes().setAll( yesButton, cancelButton );

		DialogPane pane = alert.getDialogPane();

		for( ButtonType button : alert.getButtonTypes() )
		{
			((Button) pane.lookupButton( button ) )
				.setDefaultButton( button.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE );
		}

		// --------------------------------------------------------------------
		// - Ask ... ----------------------------------------------------------
		// --------------------------------------------------------------------

		alert.showAndWait().ifPresent( r ->
		{
			// ----------------------------------------------------------------
			// - Exit ---------------------------------------------------------
			// ----------------------------------------------------------------

			if( r.getButtonData() == ButtonBar.ButtonData.YES )
			{
				Platform.exit();
			}

			// ----------------------------------------------------------------
			// - Cancel -------------------------------------------------------
			// ----------------------------------------------------------------

			else if( r.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE )
			{
				// no operation
			}
		} );
	}

	// ========================================================================
	// = Misc. ================================================================
	// ========================================================================

	/**
	 *
	 * @return
	 */
	private boolean isRunningFromJar()
	{
		Class<?> clazz = getClass();
		String clazzName = clazz.getSimpleName() + ".class";

		return clazz.getResource( clazzName ).toString().startsWith( "jar:" );
	}

	/**
	 *
	 */
	private void logRuntimeInfos()
	{
		// --------------------------------------------------------------------
		// - Diagnostic -------------------------------------------------------
		// --------------------------------------------------------------------

		LOG.config( "Operating system: "
				+ System.getProperty( "os.name" )
				+ " (" + System.getProperty( "os.version" ) + ") "
				+ System.getProperty( "os.arch" )
		);

		LOG.config( "Java VM: "
				+ System.getProperty( "java.vm.name" )
				+ " (" + System.getProperty( "java.vm.version" ) + ") "
				+ System.getProperty( "java.vm.info" )
		);

		LOG.config( "Java runtime: "
				+ System.getProperty( "java.runtime.name" )
				+ " (" + System.getProperty( "java.runtime.version" ) + ")"

		);

		LOG.config( "JavaFX runtime: "
				+ System.getProperty( "javafx.version" )
				+ " (" + System.getProperty( "javafx.runtime.version" ) + ")"
		);

		// --------------------------------------------------------------------

		LOG.config("App version: "
				+ getAppVersion().toString()
				+ " (build " + getBuildNumber() + ", " + getBuildDateTime() + ")"
		);

		// --------------------------------------------------------------------

		LOG.config( "Debug mode: " + debugMode );

		LOG.config( "Log output style: "
				+ (logAnsiOutput ? "Ansi" : "Normal")
		);

		LOG.config( "Log level: " + logLevel );
		LOG.config( "Log filter: " + (disableLogFilter ? "DISABLED" : logFilter) );
		LOG.config( "Log file: " + (disableLogfile ? "DISABLED" : logFilename) );
		if( ! disableLogfile )
		{
			LOG.config( "Keep logfile: " + (keepLogfile|debugMode)
				+ ((!keepLogfile&debugMode)? " (FORCED by debug mode)" : "") );
		}

		LOG.config( "Show log window: " + (showLogWindow|debugMode)
			+ ((!showLogWindow&debugMode)? " (FORCED by debug mode)" : "") );

		LOG.config( "Show exception window: " + showExceptionWindow );

		// --------------------------------------------------------------------

		LOG.config( "Running from jar: " + isRunningFromJar()
				+ (runningFromExe ? " [Launch4J]" : "")
		);

		LOG.config( "Locale: " + Locale.getDefault()
		);
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( FxApplication.class.getName() );

	@SuppressWarnings( "unused" )
	private static final Preferences FXAPP_PREFS
		= Preferences.userNodeForPackage( FxApplication.class );

	// ------------------------------------------------------------------------

	/*package*/ static final String RES_FQN = "be.lmenten.utils.app.fx.FxApplication";

	/*package*/ static final ResourceBundle RES
		= ResourceBundle.getBundle( RES_FQN );

	private String $( @PropertyKey( resourceBundle=RES_FQN ) String key )
	{
		return FxApplication.RES.getString( key );
	}
}
