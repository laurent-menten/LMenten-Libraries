package be.lmenten.utils.logging;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AnsiLogFormatter
	extends LogFormatter
{
	private static final String ANSI_ERROR		= "\u001B[31;1m";
	private static final String ANSI_WARNING	= "\u001B[33;1m";
	private static final String ANSI_INFO		= "\u001B[32;1m";
	private static final String ANSI_CONFIG		= "\u001B[34;1m";

	// ANSI escape code
    public static final String ANSI_RESET		= "\u001B[0m";

    public static final String ANSI_BLACK		= "\u001B[30m";
    public static final String ANSI_RED			= "\u001B[31m";
    public static final String ANSI_GREEN		= "\u001B[32m";
    public static final String ANSI_YELLOW		= "\u001B[33m";
    public static final String ANSI_BLUE		= "\u001B[34m";
    public static final String ANSI_PURPLE		= "\u001B[35m";
    public static final String ANSI_CYAN		= "\u001B[36m";
    public static final String ANSI_WHITE		= "\u001B[37m";

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public String format( LogRecord record )
	{
		StringBuilder s = new StringBuilder();

		// --------------------------------------------------------------------

		s.append( ANSI_CYAN )
			.append( record.getLongThreadID() )
			.append( ANSI_RESET )
			.append( "." )
			.append( ANSI_RED )
			.append( record.getSequenceNumber() )
			.append( ANSI_RESET )
			.append( ": " )
			;

		s.append( ANSI_PURPLE )
			.append( df.format( new Date( record.getMillis() ) ) )
			.append( ANSI_RESET )
			.append( " [" )
			.append( record.getSourceClassName() )
			.append( "." )
			.append( record.getSourceMethodName() )
			.append( "]")
			;

		// --------------------------------------------------------------------

		s.append(" ");
		s.append( getLevelColor( record.getLevel() ) )
			.append( record.getLevel() )
			.append( ANSI_RESET );

		// --------------------------------------------------------------------

		s.append( " - ")
			.append( formatMessage( record ) )
			.append("\n");

		// --------------------------------------------------------------------

		Object [] params = record.getParameters();
		if( params != null )
		{
			s.append( "\n\t" )
				.append( "Parameters : \n" );

			for( int  i = 0 ; i < params.length ; i++ )
			{
				s.append( "\t\t" )
					.append( i )
					.append( ": " )
					.append( params[i].toString() )
					.append( '\n' );
			}

			s.append( '\n' );
		}

		// --------------------------------------------------------------------

		Throwable ex = record.getThrown();
		if( ex != null )
		{
			s.append( "\n\t" )
				.append( "EXCEPTION : " );
			s.append( ANSI_RED )
				.append( ex.getClass().getName() )
			 	.append( ANSI_RESET );
			s.append( " : " )
				.append( ANSI_YELLOW )
				.append( ex.getMessage() )
			 	.append( ANSI_RESET )
				.append( "\n\n" );

			for( StackTraceElement el : ex.getStackTrace() )
			{
				s.append( '\t' )

				 .append( el.getClassName() )
				 .append( "." )
				 .append( el.getMethodName() )

				 .append( " - " );

				if( el.getFileName() != null )
				{
					s.append( "[" )
					.append( el.getFileName() )
					.append( ":" )
					.append( el.getLineNumber() )
					.append( "]" );
				}
				else
				{
					s.append( "[ ? ]" );
				}
				
				s.append( "\n" );
			}

			s.append( "\n" );
		}

		return s.toString();
	}

	// ========================================================================
	// === UTILS ==============================================================
    // ========================================================================

    public String getLevelColor( Level level )
    {
    	if( level == Level.SEVERE )
    	{
    		return ANSI_ERROR;
    	}
    	else if( level == Level.WARNING )
    	{
    		return ANSI_WARNING;
    	}
    	else if( level == Level.INFO )
    	{
    		return ANSI_INFO;
    	}

    	else if( level == Level.CONFIG )
    	{
    		return ANSI_CONFIG;
    	}

    	else if( level == Level.FINE )
    	{
    		return ANSI_CYAN;
    	}
    	else if( level == Level.FINER )
    	{
    		return ANSI_CYAN;
    	}
    	else if( level == Level.FINEST )
    	{
    		return ANSI_CYAN;
    	}

    	return ANSI_WHITE;
    }

    // ========================================================================
	// === INSTALLERS =========================================================
	// ========================================================================

	/**
	 * 
	 */
	public static AnsiLogFormatter install()
	{
		return AnsiLogFormatter.install( (Level)null );
	}

	/**
	 *
	 * @param level
	 * @return
	 */
	public static AnsiLogFormatter install( LogLevel level )
	{
		return AnsiLogFormatter.install( level.getLevel() );
	}

	/**
	 *
	 * @param level
	 * @return
	 */
	public static AnsiLogFormatter install( Level level )
	{
		AnsiLogFormatter formatter = new AnsiLogFormatter();

		Logger rootLogger = Logger.getLogger( LogFormatter.ROOT_LOGGER_NAME );
		if( level != null )
		{
			rootLogger.setLevel( level );
		}

		for( Handler rawHandler : rootLogger.getHandlers() )
		{
			if( rawHandler instanceof ConsoleHandler )
			{
				rawHandler.setFormatter( formatter );
			}

			if( level != null )
			{
				rawHandler.setLevel( level );
			}
		}

		return formatter;
	}
}
