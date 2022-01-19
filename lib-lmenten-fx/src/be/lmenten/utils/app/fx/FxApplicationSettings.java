package be.lmenten.utils.app.fx;

import be.lmenten.utils.logging.LogLevel;

public class FxApplicationSettings
{
	private final FxApplication app;

	// ========================================================================
	// =
	// ========================================================================

	public FxApplicationSettings( FxApplication app )
	{
		this.app = app;
	}

	// ========================================================================
	// =
	// ========================================================================

	public void setDebugMode( boolean debugMode )
	{
		app.debugMode = debugMode;
	}

	public void setLogAnsiOutput( boolean logAnsiOutput )
	{
		app.logAnsiOutput = logAnsiOutput;
	}

	public void setLogLevel( LogLevel logLevel )
	{
		app.logLevel = logLevel;
	}

	public void setDisableLogFilter( boolean disableLogFilter )
	{
		app.disableLogFilter = disableLogFilter;
	}

	public void setLogFilter( String logFilter )
	{
		app.logFilter = logFilter;
	}

	public void setDisableLogfile( boolean disableLogfile )
	{
		app.disableLogfile = disableLogfile;
	}

	public void setKeepLogfile( boolean keepLogfile )
	{
		app.keepLogfile = keepLogfile;
	}

	public void setShowLogWindow( boolean showLogWindow )
	{
		app.showLogWindow = showLogWindow;
	}

	public void setShowExceptionWindow( boolean showExceptionWindow )
	{
		app.showExceptionWindow = showExceptionWindow;
	}

	// ========================================================================
	// =
	// ========================================================================

	public boolean isDebugMode()
	{
		return app.debugMode;
	}

	public boolean isLogAnsiOutput()
	{
		return app.logAnsiOutput;
	}

	public LogLevel getLogLevel()
	{
		return app.logLevel;
	}

	public boolean isDisableLogFilter()
	{
		return app.disableLogFilter;
	}

	public String getLogFilter()
	{
		return app.logFilter;
	}

	public boolean isDisableLogfile()
	{
		return app.disableLogfile;
	}

	public boolean isKeepLogfile()
	{
		return app.keepLogfile;
	}

	public boolean isShowLogWindow()
	{
		return app.showLogWindow;
	}

	public boolean isShowExceptionWindow()
	{
		return app.showExceptionWindow;
	}

	public boolean isRunningFromExe()
	{
		return app.runningFromExe;
	}
}
