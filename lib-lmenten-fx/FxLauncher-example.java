package be.lmenten.bayeux;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import javafx.application.Application;

/**
 * <p>
 * Launcher for self-contained modular JavaFx application from jar.
 * </p>
 *
 * <^p>
 * Uses javaFx from lib/fx directory if it exists or from from PATH_TO_FX
 * environment variable.
 * </p>
 *
 * @version 1.0.1
 * @since 2022-01-02
 * @author <A HREF="mailto:laurent.menten@gmail.com">Laurent MENTEN</A>
 */
public class FxLauncher
{
	// ========================================================================
	// = Config ===============================================================
	// ========================================================================

	private static final String [] fxModules = {
		"javafx.base",
		"javafx.graphics",
		"javafx.controls",
		"javafx.web",
		"javafx.fxml",
	};

	private static final String [] appModules = {
		"lib.lmenten",
		"lib.lmenten.fx",
//		"org.controlsfx.controls",
		"org.jetbrains.annotations",
	};

	private static final String [] moduleExports = {
	};

	private static final String [] debugProfile = {
		"-Dbe.lmenten.debug=true",
		"-Dbe.lmenten.ansiLogOutput=true",
		"-Dbe.lmenten.logLevel=ALL",
		"-Dbe.lmenten.logFilter=be.lmenten.*",
		"-Dbe.lmenten.showLogWindow=true",
	};

	// ========================================================================
	// = Proxy class ==========================================================
	// ========================================================================

	/**
	 * Proxy to avoid unresolved fx classes error at launcher run.
	 */
	static class Proxy
	{
		public static void main( String[] args )
		{
			long start = System.currentTimeMillis();

			Application.launch( !!! .class !!!, args );

			long duration = System.currentTimeMillis() - start;

			// ----------------------------------------------------------------

			long millis = duration % 1000;
			long second = (duration / 1000) % 60;
			long minute = (duration / (1000 * 60)) % 60;
			long hour = (duration / (1000 * 60 * 60)) % 24;
			String time = String.format( "%02d:%02d:%02d.%d", hour, minute, second, millis );

			LOG.info( "Application finished." );
			LOG.info( "Running time : " + time );

			// ----------------------------------------------------------------

			if( debugMode )
			{
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	// ========================================================================
	// = Main =================================================================
	// ========================================================================

	private static final boolean debugMode
		= Boolean.parseBoolean(
			System.getProperty( "be.lmenten.debug", "false" )
	);

	// ------------------------------------------------------------------------

	public static void main( String[] args )
			throws URISyntaxException, IOException
	{
		final List<String> javaArgs = new ArrayList<>();

		// --------------------------------------------------------------------
		// - Check OS ---------------------------------------------------------
		// --------------------------------------------------------------------

		boolean isWindows =
			System.getProperty( "os.name" ).toLowerCase().contains( "win" );

		// --------------------------------------------------------------------
		// - Check if running from jar ----------------------------------------
		// --------------------------------------------------------------------

		String protocol =
			Objects.requireNonNull( Proxy.class.getResource("") ).getProtocol();

		boolean runsFromJar = "jar".equalsIgnoreCase( protocol );

		// --------------------------------------------------------------------
		// - Java VM executable path ------------------------------------------
		// --------------------------------------------------------------------

		String java = System.getProperty( "java.home" )
				+ File.separator + "bin"
				+ File.separator + "java"
				;

		if( isWindows )
		{
			java += ".exe";
		}

		javaArgs.add( java );

		// --------------------------------------------------------------------
		// - Set acceptable defaults for debug mode ---------------------------
		// --------------------------------------------------------------------

		if( debugMode )
		{
			javaArgs.addAll( Arrays.asList( debugProfile ) );
		}

		// --------------------------------------------------------------------
		// - Add original VM arguments ----------------------------------------
		// --------------------------------------------------------------------

		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArgs = runtime.getInputArguments();

		javaArgs.addAll( jvmArgs );

		// --------------------------------------------------------------------
		// - Working directory ------------------------------------------------
		// --------------------------------------------------------------------

		URI jarUrl = Proxy.class.getProtectionDomain().getCodeSource().getLocation().toURI();

		File jarFile = new File( jarUrl );
		String jarPath = jarFile.getParentFile().getAbsolutePath();

		// --------------------------------------------------------------------

		String workingDir;
		if( runsFromJar )
		{
			workingDir = jarFile.getParentFile().getCanonicalPath();
		}
		else
		{
			workingDir = new File( "." ).getCanonicalPath();
		}

		// --------------------------------------------------------------------
		// - module-path, module lists and exports ----------------------------
		// --------------------------------------------------------------------

		String fxDirName =
			workingDir
				+ File.separator + "lib" + File.separator + "fx"
				+ File.separator + "lib"
			;

		File fxFile = new File( fxDirName );
		if( ! fxFile.exists() )
		{
			fxDirName = System.getenv( "PATH_TO_FX" );
			if( fxDirName == null )
			{
				error( "JavaFx PATH_TO_FX environment variable not found.", null );
			}
		}

		String modulePath =
			workingDir
				+	File.pathSeparator + workingDir + File.separator + "lib"
				+	File.pathSeparator + workingDir + File.separator + "plugins"
				+	File.pathSeparator + fxDirName
			;

		javaArgs.add( "--module-path" );
		javaArgs.add( "\"" +modulePath + "\"" );

		// --------------------------------------------------------------------

		if( appModules.length > 0 )
		{
			String appModulesList = String.join( ",", appModules );

			javaArgs.add( "--add-modules" );
			javaArgs.add( appModulesList );
		}

		if( fxModules.length > 0 )
		{
			String fxModulesList = String.join( ",", fxModules );

			javaArgs.add( "--add-modules" );
			javaArgs.add( fxModulesList );
		}

		if( moduleExports.length > 0 )
		{
			String moduleExportsList = String.join( ",", moduleExports );

			javaArgs.add( "--add-exports" );
			javaArgs.add( moduleExportsList );
		}

		// --------------------------------------------------------------------
		// - Plugins list + Classpath and class name for proxy ----------------
		// --------------------------------------------------------------------

		String pluginsList = "";

		File pluginsDir = new File(workingDir + File.separator + "plugins" );
		if( pluginsDir.exists() )
		{
			File[] plugins = pluginsDir.listFiles( ( file, name )
					-> file.isFile() && name.toLowerCase().endsWith( ".jar" )
			);

			for( File plugin : plugins )
			{
				pluginsList += File.pathSeparator + plugin.getAbsolutePath();
			}
		}
		// --------------------------------------------------------------------

		javaArgs.add( "-classpath" );
		if( runsFromJar )
		{
			javaArgs.add( "\"" + jarFile.getAbsolutePath() + File.pathSeparator + pluginsList + "\"" );
		}
		else
		{
			javaArgs.add( "\"" + jarPath + File.pathSeparator + pluginsList + "\"" );
		}

		javaArgs.add( Proxy.class.getName() );

		// --------------------------------------------------------------------
		// - Add program arguments --------------------------------------------
		// --------------------------------------------------------------------

		Collections.addAll( javaArgs, args );

		// --------------------------------------------------------------------
		// - Debug ------------------------------------------------------------
		// --------------------------------------------------------------------

		if( debugMode )
		{
			System.out.println( "Working directory: '" + workingDir + "'." );

			for( int i = 0 ; i < javaArgs.size() ;  i++ )
			{
				System.out.printf( "   %2d: %s\n", i, javaArgs.get( i ) );
			}
		}

		// --------------------------------------------------------------------
		// - Run process ------------------------------------------------------
		// --------------------------------------------------------------------

		try
		{
			ProcessBuilder pb = new ProcessBuilder( javaArgs );
			pb.directory( new File( workingDir ) );
			pb.inheritIO();

			Process p = pb.start();

			int exitCode = p.waitFor();

			System.exit( exitCode );
		}
		catch( Exception ex )
		{
			error( "Failed to launch application process.", ex );
		}
	}

	// ========================================================================
	// = Error handling =======================================================
	// ========================================================================

	private static void error( String message, Throwable ex )
	{
		if( GraphicsEnvironment.isHeadless() )
		{
			System.err.println( message );

			if( ex != null )
			{
				ex.printStackTrace();
			}
		}
		else
		{
			if( ex != null )
			{
				message += "\n" + ex.getMessage();
			}

			JOptionPane.showMessageDialog( null,
					message,
					Proxy.class.getSimpleName(),
					JOptionPane.ERROR_MESSAGE
			);
		}

		System.exit( -1 );
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( !!! .class !!!.getName() );
}
