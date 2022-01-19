package be.lmenten.utils.app.fx;

import be.lmenten.utils.logging.AnsiLogFormatter;
import be.lmenten.utils.logging.LogFormatter;
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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Runtime.Version;

/**
 * An abstract JavaFx application base class with various capabilities like:
 * <ul>
 *     <li>Debug</li>
 *     <li>Logging</li>
 * </ul>
 * <ul>
 *     <li>be.lmenten.debug</li>
 *     <li>be.lmenten.logLevel</li>
 *     <li>be.lmenten.logFilter</li>
 *     <li>be.lmenten.logDir</li>
 *     <li>be.lmenten.logFileEnabled</li>
 *     <li>be.lmenten.keepLogFile</li>
 *     <li>be.lmenten.ansiLogOutput</li>
 *     <li>be.lmenten.showLogWindow</li>
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
	private final FxApplicationSettings settings
		= new FxApplicationSettings( this );

	private final boolean runningFromJar;

	// ------------------------------------------------------------------------

	private boolean logFileOK;
	private String logFilename = null;

	protected Stage stage;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	protected FxApplication()
	{
		// --------------------------------------------------------------------
		// - Custom log output formatter --------------------------------------
		// --------------------------------------------------------------------

		if( settings.isAnsiLogOutputEnabled() )
		{
			AnsiLogFormatter.install( settings.getLogLevel() );
		}
		else
		{
			LogFormatter.install( settings.getLogLevel() );
		}

		LOG.fine( "Starting application " + getClass().getSimpleName() );

		LOG.finer( "Console log formatter installed." );

		// --------------------------------------------------------------------
		// - Are we running from JAR file -------------------------------------
		// --------------------------------------------------------------------

		String protocol =
			Objects.requireNonNull( getClass().getResource("") ).getProtocol();

		runningFromJar = "jar".equalsIgnoreCase( protocol );
	}

	// ========================================================================
	// =
	// ========================================================================

	public abstract String getAppTitle();
	public abstract Version getAppVersion();
	public abstract long getBuildNumber();
	public abstract LocalDateTime getBuildDateTime();

	public final boolean isRunningFromJar()
	{
		return runningFromJar;
	}

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
	 * @throws Exception if something goes wrong
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

		if( settings.isLogFileEnabled() )
		{
			File logDir = new File( settings.getLogDir() );
			if( ! logDir.exists() )
			{
				LOG.finer( "Log folder \"" + settings.getLogDir() + "\" does not exists." );

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
				logFileOK = logFilename != null;
				if( ! logFileOK )
				{
					LOG.warning("Could not install log file handle.");
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

		LOG.finer( "Installing log source filter" );

		LogRegExPackageFilter.install( settings.getLogFilter(), settings.getLogLevel() );
	}

	// ========================================================================
	// = Fx start =============================================================
	// ========================================================================

	/**
	 * Call this first in application start(stage) method.
	 *
	 * @param stage the application stage
	 * @throws Exception if something goes wrong
	 */
	@Override
	public void start( Stage stage )
		throws Exception
	{
		LOG.fine( "FxApplication.start() ..." );

		this.stage = stage;

		// --------------------------------------------------------------------
		// - First open log window if needed ----------------------------------
		// --------------------------------------------------------------------

		if( settings.isDebugModeEnabled() || settings.isShowLogWindowEnabled() )
		{
			LogWindow.display();
		}

		logRuntimeInfos();

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------

		stage.setOnCloseRequest( this::closeRequest );
	}

	// ========================================================================
	// = Fx cleanup ===========================================================
	// ========================================================================

	/**
	 * Call this last in application stop() method.
	 *
	 * @throws Exception if something goes wrong
	 */
	@Override
	public void stop()
		throws Exception
	{
		LOG.fine( "FxApplication.stop() ..." );

		// ------------------------------------------------------------
		// - If not in debug mode remove log file ---------------------
		// ------------------------------------------------------------

		if( settings.isLogFileEnabled() )
		{
			if( logFileOK )
			{
				if( !settings.isDebugModeEnabled() && !settings.isKeepLogFileEnabled() )
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
	 * @param ev triggering event
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
		alert.setHeaderText( RESOURCE.getString( "alert.quit.header" ) );
		alert.setContentText( RESOURCE.getString( "alert.quit.content.nosave" ) );

		ButtonType yesButton = new ButtonType( RESOURCE.getString("yes"), ButtonBar.ButtonData.YES );
		ButtonType cancelButton = new ButtonType( RESOURCE.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE );
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
				LOG.fine( "Canceled close request." );
			}
		} );
	}

	// ========================================================================
	// = Misc. ================================================================
	// ========================================================================

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

		LOG.config( "Running from JAR: "
			+ (isRunningFromJar() ? "Yes" : "No" )
		);

		// --------------------------------------------------------------------

		LOG.config( "Debug mode: " + settings.isDebugModeEnabled() );

		LOG.config( "Log output style: "
			+ (settings.isAnsiLogOutputEnabled() ? "Ansi" : "Normal")
		);

		LOG.config( "Log level: " + settings.getLogLevel() );
		LOG.config( "Log filter: " + settings.getLogFilter() );
		LOG.config( "Log file: " + (settings.isLogFileEnabled() ? logFilename : "DISABLED" ) );
		if( settings.isLogFileEnabled() )
		{
			LOG.config( "Keep logfile: " + (settings.isKeepLogFileEnabled()|settings.isDebugModeEnabled())
				+ ((!settings.isKeepLogFileEnabled()&settings.isDebugModeEnabled())? " (FORCED by debug mode)" : "") );
		}

		LOG.config( "Show log window: " + (settings.isShowLogWindowEnabled()|settings.isDebugModeEnabled())
			+ ((!settings.isShowLogWindowEnabled()&settings.isDebugModeEnabled())? " (FORCED by debug mode)" : "") );

		LOG.config( "Show exception window: " + settings.isShowExceptionWindowEnabled() );

		// --------------------------------------------------------------------

		LOG.config( "Locale: " + Locale.getDefault() );
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( FxApplication.class.getName() );

	// ------------------------------------------------------------------------

	/*package*/ static final String RESOURCE_FQN
		= "be.lmenten.utils.app.fx.FxApplication";

	/*package*/ static final ResourceBundle RESOURCE
		= ResourceBundle.getBundle( RESOURCE_FQN );

	@SuppressWarnings( "unused" )
	private String $( @PropertyKey( resourceBundle = RESOURCE_FQN ) String key )
	{
		return FxApplication.RESOURCE.getString( key );
	}
}
