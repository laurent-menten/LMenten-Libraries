package be.lmenten.utils.arch.win.registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WinRegistry
{
	/**
	 * 
	 * @param key path in the registry
	 * @param valueName registry key
	 * @return registry value or null if not found
	 */
	public static final RegistryEntry readRegistry( String key, String valueName )
	{
		final String CMD_REG_QUERY
			= "reg query"
				+ " \"" + key + "\""
				+ " /v "
				+ "\"" + valueName + "\"";
		try
		{

			Process process = Runtime.getRuntime().exec( CMD_REG_QUERY );

			StreamReader reader = new StreamReader( process.getInputStream(), process.getErrorStream() );
			reader.start();
			process.waitFor();
			reader.join();

			if( process.exitValue() != 0 )
			{
				String strerror = reader.getError();
				LOG.severe( "REG QUERY: " + strerror );

				return null;
			}

			/* Expected return format:
			 * 		\n
			 * 		location\n
			 * 		name type value\n
			 * 		\n
			 */

			String strout = reader.getOutput();
			String [] lines = strout.split( "\n" );
			if( lines.length != 4 )
			{	
				LOG.warning( "REG QUERY did not return 4 lines, "
							+ lines.length + " found" );
				return null;
			}

			StringTokenizer tokenizer = new StringTokenizer( lines[2].strip() );
			if( tokenizer.countTokens() < 2 )
			{
				LOG.warning( "REG QUERY value line doesn't have at least 2 tokens, "
						+ tokenizer.countTokens() + " found" );
				return null;
			}

			String [] tokens = new String[ tokenizer.countTokens() ];
			for( int i = 0 ; tokenizer.hasMoreElements() ; i++ )
			{
				tokens[i] = tokenizer.nextToken();
			}

			RegistryEntryType type = RegistryEntryType.valueOf( tokens[1] );
			if( type == RegistryEntryType.REG_NONE )
			{
				return new RegistryEntry( key, valueName, type );
			}
			else
			{
				return new RegistryEntry( key, valueName, type, tokens[2] );
			}

		}
		catch( Exception ex )
		{
			LOG.log( Level.SEVERE, CMD_REG_QUERY, ex );
			return null;
		}
	}

	// ========================================================================
	// = 
	// ========================================================================

	static class StreamReader
		extends Thread
	{
		private InputStream out;
		private InputStream err;

		private StringWriter outSw= new StringWriter();
		private StringWriter errSw= new StringWriter();

		public StreamReader( InputStream out, InputStream err )
		{
			this.out = out;
			this.err = err;
		}

		public void run()
		{
			try
			{
				int c;
				while( (c = out.read()) != -1 )
				{
					outSw.write(c);
				}

				while( (c = err.read()) != -1 )
				{
					errSw.write(c);
				}
			}
			catch( IOException e )
			{
			}
		}

		public String getOutput()
		{
			return outSw.toString();
		}

		public String getError()
		{
			return errSw.toString();
		}
	}

	private static final Logger LOG
		= Logger.getLogger( WinRegistry.class.getName() );
}
