package be.lmenten.utils.math.evaluator.utils;

public interface ParseUtils
{
	// ------------------------------------------------------------------------
	// - Integer number -------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Convert a binary number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('b'|'B') ('_' | digit2*)* digit2</li>
	 *     <li>digit2 ('_' | digit2*)* digit2 ('b'|'B')</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseBinary( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		if( string.toLowerCase().startsWith( "0b" ) )
		{
			offset = 2;
		}
		else if( string.toLowerCase().endsWith( "b" ) )
		{
			limit--;
		}

		return parseInteger( string, offset, limit,2 );
	}

	/**
	 * Convert a octal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('_' | digit8*)* digit8+</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseOctal( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		return parseInteger( string, offset, limit,8 );
	}

	/**
	 * Convert a decimal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0'</li>
	 *     <li>digit10_not0</li>
	 *     <li>digit10_not0 ('_' | digit10*)* digit10+</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseDecimal( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		return parseInteger( string, offset, limit,10 );
	}

	/**
	 * Convert a hexadecimal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('x'|'X') ('_' | digit16*)* digit16</li>
	 *     <li>'$' ('_' | digit16*)* digit16</li>
	 *     <li>'#' ('_' | digit16*)* digit16</li>
	 *     <li>digit10 ('h'|'H')</li>
	 *     <li>digit10 ('_' | digit16*)* digit16+ ('h'|'H')</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseHexadecimal( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		if( string.toLowerCase().startsWith( "$" )
			|| string.toLowerCase().startsWith( "#" ) )
		{
			offset = 1;
		}
		else if( string.toLowerCase().startsWith( "0x" ) )
		{
			offset = 2;
		}
		else if( string.toLowerCase().endsWith( "h" ) )
		{
			limit--;
		}

		return parseInteger( string, offset, limit,16 );
	}

	// ------------------------------------------------------------------------

	/**
	 * Convert number in a substring delimited by offset and limit to its
	 * integer value according to the required radix.
	 * <p>
	 * NOTE: As this method is a support for SabledCC generated parsers, the
	 * number is supposed to be correctly formatted and no check is made to
	 * detect radix/content related problems.
	 *
	 * @param string the string containing the number representation
	 * @param offset offset of number in string (after its prefix)
	 * @param radix radix of the number representation
	 * @return the number value
	 * @throws NumberFormatException if the number representation is invalid
	 */
	private static long parseInteger( String string, int offset, int limit, int radix )
		throws NumberFormatException
	{
		long value = 0L;
		long sign = 1;

		if( radix == 10 && string.startsWith( "-" ) )
		{
			offset++;
			sign = -1;
		}

		for( int i = offset ; i < limit ; i++ )
		{
			char c = string.charAt( i );
			switch( c )
			{
				case '_':
				{
					// Ignore cosmetic separator
					break;
				}

				case '0', '1', '2', '3', '4', '5', '6', '7','8', '9':
				{
					value = (value * radix) + (c - '0');
					break;
				}

				case 'a', 'b','c','d','e','f':
				{
					value = (value * radix) + (10 + (c - 'a'));
					break;
				}

				case 'A', 'B','C','D','E','F':
				{
					value = (value * radix) + (10 + (c - 'A'));
					break;
				}

				default:
				{
					throw new NumberFormatException();
				}
			}

		}

		return sign * value;
	}

	// ------------------------------------------------------------------------
	// - Floating point number ------------------------------------------------
	// ------------------------------------------------------------------------

	public static double parseFloatingPoint( String string )
		throws NumberFormatException
	{
		return Double.parseDouble( string );
	}

	public static double parseFraction( String string )
	{
		double value = 0.;
		double sign = 1.;

		String [] parts = string.split( "#" );

		if( parts[0].toLowerCase().startsWith( "-" ) )
		{
			sign = -1;
			value = parseDecimal( parts[ 0 ].substring( 1 ) );
		}
		else
		{
			value = parseDecimal( parts[ 0 ] );
		}

		if( parts.length == 2 )
		{
			value = value / parseDecimal( parts[ 1 ] );
		}
		else // parts.length == 3
		{
			double frac = parseBinary( parts[ 1 ] );
			frac = frac / parseDecimal( parts[ 2 ] );

			value = value + frac;
		}

		return sign * value;
	}

	public static String indicator( int pos )
	{
		return "-".repeat( pos-1 ) + "^";
	}
}
