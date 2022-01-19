package be.lmenten.utils.lang;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class ExceptionUtils
{
	private static final String CAUSE_CAPTION = "Caused by: ";
	private static final String SUPPRESSED_CAPTION = "Suppressed: ";

	// ========================================================================
	// = Stack trace ==========================================================
	// ========================================================================

	/**
	 * Get the stack trace of the given throwable in a string.
	 * 
	 * @param t
	 */
	public static String toString( Throwable t )
	{
		return toStringBuilder( t ).toString();
	}
	
	/**
	 * Get the stack trace of the given throwable in a string builder.
	 * 
	 * @param t
	 * @return
	 */
	public static StringBuilder toStringBuilder( Throwable t )
	{
		return append( t, new StringBuilder() );
	}

	/**
	 * Append the stack trace of the given throwable to the given string builder.
	 * 
	 * @param t
	 * @param s
	 * @return
	 */
	public static StringBuilder append( Throwable t, StringBuilder s )
	{
		Set<Throwable> dejaVu
			= Collections.newSetFromMap( new IdentityHashMap<>() );

		dejaVu.add( t );

		s.append( t.getClass().getName() );
		s.append( " --> " );
		s.append( t.getMessage() );
		s.append( "\n" );

		StackTraceElement [] trace = t.getStackTrace();
		for( StackTraceElement traceElement : trace )
		{
			s.append( "   at " );
			printStackTraceElement( s, traceElement );
			s.append( "\n" );
		}

		for( Throwable se : t.getSuppressed() )
		{
			s.append( "\n" );

			printEnclosedStackTrace( se, trace, SUPPRESSED_CAPTION, "   ", dejaVu, s );
		}

		Throwable ourCause = t.getCause();
		if( ourCause != null )
		{
			s.append( "\n" );

			printEnclosedStackTrace( ourCause, trace, CAUSE_CAPTION, "", dejaVu, s );
		}

		return s;
	}

	// ========================================================================
	// = Internals ============================================================
	// ========================================================================

	/**
	 * 
	 * @param t
	 * @param enclosingTrace
	 * @param caption
	 * @param prefix
	 * @param dejaVu
	 * @param s
	 */
	private static void printEnclosedStackTrace( Throwable t,
			StackTraceElement[] enclosingTrace,
			String caption,
			String prefix,
			Set<Throwable> dejaVu,
			StringBuilder s )
	{
		if( dejaVu.contains( t ) )
		{
			s.append( prefix );
			s.append( caption );
			s.append( "[CIRCULAR REFERENCE: " );
			s.append( 	t.getClass().getName() );
			s.append( "]" );
			s.append( "\n" );
		}
		else
		{
			dejaVu.add( t );

			// Compute number of frames in common between this and enclosing trace

			StackTraceElement[] trace = t.getStackTrace();
			int m = trace.length - 1;
			int n = enclosingTrace.length - 1;
			while( m >= 0 && n >=0 && trace[m].equals( enclosingTrace[n] ) )
			{
				m--; n--;
			}

			int framesInCommon = trace.length - 1 - m;

			s.append( prefix );
			s.append( caption );
			s.append( t.getClass().getName() );
			s.append( " --> " );
			s.append( t.getMessage() );
			s.append( "\n" );

			for( int i = 0 ; i <= m ; i++ )
			{
				s.append( prefix );
				s.append( "   at " );
				printStackTraceElement( s, trace[i] );
				s.append( "\n" );
			}

			if( framesInCommon != 0 )
			{
				s.append( prefix );
				s.append( "   ... " );
				s.append( "" + framesInCommon );
				s.append( " more\n" );

			}

			for( Throwable se : t.getSuppressed() )
			{
				s.append( "\n" );

				printEnclosedStackTrace( se, trace, SUPPRESSED_CAPTION, prefix + "   ", dejaVu, s );
			}

			Throwable ourCause = t.getCause();
			if( ourCause != null )
			{
				s.append( "\n" );

				printEnclosedStackTrace( ourCause, trace, CAUSE_CAPTION, prefix, dejaVu, s );
			}
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param s
	 * @param element
	 */
	private static void printStackTraceElement( StringBuilder s, StackTraceElement element )
	{
		if( element.getModuleName() != null )
		{
			s.append( "[" );
			s.append( element.getModuleName() );

			if( element.getModuleVersion() != null )
			{
				s.append( " (" );
				s.append( element.getModuleVersion() );
				s.append( ")" );
			}

			s.append( "] " );
		}

		// --------------------------------------------------------------------

		s.append( element.getClassName() );
		s.append( "." );
		s.append( element.getMethodName() );

		if( element.getFileName() != null )
		{
			s.append( " (" );
			s.append( element.getFileName() );
			if( ! element.isNativeMethod() )
			{
				s.append( ":" );
				s.append( element.getLineNumber() );
			}
			s.append( ")" );
		}
			
		// --------------------------------------------------------------------

		if( element.isNativeMethod() )
		{
			s.append( " [NATIVE]" );
		}

		if( element.getClassLoaderName() != null )
		{
			s.append( " [classloader = " );
			s.append( element.getClassLoaderName() );
			s.append( "]" );
		}
	}
}
