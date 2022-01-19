package be.lmenten.utils.lang;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class StringUtils
{
	public static String leftPadding( String input, int L )
	{
		return String.format( "%" + L + "s", input );
	}

	public static String rightPadding( String input, int L )
	{
		return String.format( "%" + (-L) + "s", input );
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static long parseNumber( String s )
	{
		int index = 0;
		int radix = 10;
		long value = 0;

		// --------------------------------------------------------------------

		if( (s == null) || (s.length() <= 0) )
		{
			return 0l;
		}

		// --------------------------------------------------------------------

		char c = s.charAt( index++ );
		if( c == '0' )
		{
			radix = 8;

			if( index >= s.length() )
			{
				return 0l;
			}

			c = s.charAt( index );
			if( (c == 'b') || (c == 'B' ) )
			{
				radix = 2;
				index++;
			}
			else if( (c == 'x') || (c == 'X' ) )
			{
				radix = 16;
				index++;
			}
		}
		else if( (c == '$') | (c == '#') )
		{
			radix = 16;
		}
		else
		{
			value = (c - '0');
			if( (value < 0) || (value >= radix) )
			{
				throw new NumberFormatException();
			}
		}

		// --------------------------------------------------------------------

		while( index < s.length() )
		{
			c = s.charAt( index++ );

			long tmp = charToValue( c );			
			if( (tmp < 0) || (tmp >= radix) )
			{
				if( c == '_' )
				{
					continue;
				}

				else if( radix == 2 )
				{
					if( (c == 'b') || (c == 'B') )
					{
						break;
					}
				}
				else if( radix == 10 )
				{
					if( c == 'K' )
					{				
						value *= 1024;
						break;
					}
					else if( c == 'M' )
					{					
						value *= 1024*1024;
						break;
					}
					else if( c == 'G' )
					{					
						value *= 1024*1024*1024;
						break;
					}
					else if( c == 'T' )
					{					
						value *= 1024*1024*1024*1024;
						break;
					}
				}

				else if( radix == 16 )
				{
					if( (c == 'h') || (c == 'H') )
					{
						break;
					}
				}

				throw new NumberFormatException();

			}

			value = (value * radix) + tmp;
		}

		return value;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public static long parseNumber( String s, int radix )
	{
		long value = 0;

		// --------------------------------------------------------------------

		if( (s == null) || (s.length() <= 0) )
		{
			return 0l;
		}

		// --------------------------------------------------------------------

		for( int index = 0 ; index < s.length() ; index++ )
		{
			char c = s.charAt( index );

			long tmp = charToValue( c );
			if( (tmp < 0) || (tmp >= radix) )
			{
				throw new NumberFormatException();				
			}

			if( c == '_' )
			{
				continue;
			}

			value = (value * radix) + tmp;
		}

		return value;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public static Boolean parseBoolean( String s )
	{
		return Boolean.parseBoolean( s );
	}
	
	// ========================================================================
	// ===
	// ========================================================================

	public static int charToValue( char c )
	{
		if( (c >= '0') && (c <= '9') )
			return c - '0';

		if( (c >= 'A') && (c <= 'F') )
			return 10 + c - 'A';

		if( (c >= 'a') && (c <= 'f') )
			return 10 + c - 'a';

		return -1;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public static String toBinaryString( int data, int bits )
	{
		StringBuilder s = new StringBuilder();
		
		for( int mask = (1 << (bits-1)) ; mask != 0 ; mask >>= 1 )
		{
			s.append( ((data & mask) == mask) ? '1' : '0' );

			if( (mask == 0x10) || (mask == 0x100) || (mask == 0x1000) )
			{
				s.append( "_" );
			}
		}
	
		return s.toString();
	}

	// ------------------------------------------------------------------------

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String toHexString( byte[] bytes )
	{
		StringBuilder s = new StringBuilder();

		for( byte b : bytes )
		{
			s.append( HEX_ARRAY[(b >> 4) & 0x0F] )
			 .append( HEX_ARRAY[ b       & 0x0F] )
			 ;
		}

	    return s.toString();
	}
}
