package be.lmenten.utils.lang;

import java.util.Objects;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class NumberUtils
{
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static int propagateByteMSB( int b )
	{
		int value = b & 0xFF;
		if( ((value & 0b1000_0000) == 0b1000_0000) )
		{
			value |=  ~0xFF;
		}

		return value;
	}

	// ========================================================================
	// = Single bit access ====================================================
	// ========================================================================

	/**
	 * Get a single bit from a byte.
	 * 
	 * @param data
	 * @param bit
	 * @return
	 */
	public static boolean bit( byte data, int bit )
	{
		if( (bit < 0) || (bit >= 8) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for byte." );
		}

		return ((data & (1<<bit)) == (1<<bit));
	}

	/**
	 * Get a single bit from a short.
	 * 
	 * @param data
	 * @param bit
	 * @return
	 */
	public static boolean bit( short data, int bit )
	{
		if( (bit < 0) || (bit >= 16) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for short." );
		}

		return ((data & (1<<bit)) == (1<<bit));
	}

	/**
	 * Get a single bit from an int.
	 * 
	 * @param data
	 * @param bit
	 * @return
	 */
	public static boolean bit( int data, int bit )
	{
		if( (bit < 0) || (bit >= 32) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for int." );
		}

		return ((data & (1<<bit)) == (1<<bit));
	}

	/**
	 * Get a single bit from a long.
	 * 
	 * @param data
	 * @param bit
	 * @return
	 */
	public static boolean bit( long data, int bit )
	{
		if( (bit < 0) || (bit >= 64) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for long." );
		}

		return ((data & (1<<bit)) == (1<<bit));
	}

	// ------------------------------------------------------------------------

	public static boolean bit0( int data ) { return bit( data, 0 ); }
	public static boolean bit1( int data ) { return bit( data, 1 ); }
	public static boolean bit2( int data ) { return bit( data, 2 ); }
	public static boolean bit3( int data ) { return bit( data, 3 ); }
	public static boolean bit4( int data ) { return bit( data, 4 ); }
	public static boolean bit5( int data ) { return bit( data, 5 ); }
	public static boolean bit6( int data ) { return bit( data, 6 ); }
	public static boolean bit7( int data ) { return bit( data, 7 ); }

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * Set a single bit in byte.
	 * 
	 * @param data
	 * @param bit
	 * @param state
	 * @return
	 */
	public static byte bit( byte data, int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= 8) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for byte." );
		}

		return (byte) ((state) ? data | (1<<bit) : data & ~(1<<bit));
	}

	/**
	 * Set a single bit in short.
	 * 
	 * @param data
	 * @param bit
	 * @param state
	 * @return
	 */
	public static short bit( short data, int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= 16) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for short." );
		}

		return (short) ((state) ? data | (1<<bit) : data & ~(1<<bit));
	}

	/**
	 * Set a single bit in int.
	 * 
	 * @param data
	 * @param bit
	 * @param state
	 * @return
	 */
	public static int bit( int data, int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= 32) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for int." );
		}

		return (state) ? data | (1<<bit) : data & ~(1<<bit);
	}

	/**
	 * Set a single bit in long.
	 * 
	 * @param data
	 * @param bit
	 * @param state
	 * @return
	 */
	public static long bit( long data, int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= 64) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range for long." );
		}

		return (state) ? data | (1<<bit) : data & ~(1<<bit);
	}

	// ------------------------------------------------------------------------

	public static int bit0( int data, boolean state ) { return bit( data, 0, state ); }
	public static int bit1( int data, boolean state ) { return bit( data, 1, state ); }
	public static int bit2( int data, boolean state ) { return bit( data, 2, state ); }
	public static int bit3( int data, boolean state ) { return bit( data, 3, state ); }
	public static int bit4( int data, boolean state ) { return bit( data, 4, state ); }
	public static int bit5( int data, boolean state ) { return bit( data, 5, state ); }
	public static int bit6( int data, boolean state ) { return bit( data, 6, state ); }
	public static int bit7( int data, boolean state ) { return bit( data, 7, state ); }

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param number
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseBinary( String number )
		throws NumberFormatException
	{
		Objects.requireNonNull( number );

		// --------------------------------------------------------------------

		int lenght = number.length();
		int index = 0;

		long value = 0l;

		// --------------------------------------------------------------------

		// 0 [b|B] [0|1]+

		if( number.startsWith( "0b") | number.startsWith( "0B" ) )
		{
			index = 2;
		}

		// [0|1]+ [b|B]

		else if( number.endsWith( "b") || number.endsWith( "B" ) )
		{
			lenght -= 1;
		}

		// --------------------------------------------------------------------

		for( int i = index; i < lenght ; i++ )
		{
			char c = number.charAt( i );
			switch( c )
			{
				case '_':
					break;

				case '0', '1':
					value = (value * 2) + (c - '0');
					break;

				default:
					String text = String.format( "Invalid character '%c' for binary at position %d in string '%s' [%d,%d].",
						c, i, number, index, lenght );
					throw new NumberFormatException( text );
			}
		}

		// --------------------------------------------------------------------

		return value;
	}

	/**
	 * 
	 * @param number
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseOctal( String number )
		throws NumberFormatException
	{
		Objects.requireNonNull( number );

		// --------------------------------------------------------------------

		int lenght = number.length();
		int index = 0;

		long value = 0l;

		// --------------------------------------------------------------------

		for( int i = index; i < lenght ; i++ )
		{
			char c = number.charAt( i );
			switch( c )
			{
				case '_':
					break;

				case '0', '1', '2', '3', '4', '5', '6', '7':
					value = (value * 8) + (c - '0');
					break;

				default:
					String text = String.format( "Invalid character '%c' for octal at position %d in string '%s' [%d,%d].",
						c, i, number, index, lenght );
					throw new NumberFormatException( text );
			}
		}

		// --------------------------------------------------------------------

		return value;
	}

	/**
	 * µ
	 * @param number
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseDecimal( String number )
		throws NumberFormatException
	{
		Objects.requireNonNull( number );

		// --------------------------------------------------------------------

		int lenght = number.length();
		int index = 0;

		long value = 0l;

		// --------------------------------------------------------------------

		// [0..9]+ [d|D]

		if( number.endsWith( "d") || number.endsWith( "D" ) )
		{
			lenght -= 1;
		}

		// --------------------------------------------------------------------

		for( int i = index; i < lenght ; i++ )
		{
			char c = number.charAt( i );
			switch( c )
			{
				case '_':
					break;

				case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
					value = (value * 10) + (c - '0');
					break;

				default:
					String text = String.format( "Invalid character '%c' for decimal at position %d in string '%s' [%d,%d].",
						c, i, number, index, lenght );
					throw new NumberFormatException( text );
			}
		}

		// --------------------------------------------------------------------

		return value;
	}

	public static long parseHexadecimal( String number )
	{
		Objects.requireNonNull( number );

		// --------------------------------------------------------------------

		int lenght = number.length();
		int index = 0;

		long value = 0l;

		// --------------------------------------------------------------------

		// $ [0..9|a..f|A..F]+ | # [0..9|a..f|A..F]+
		
		if( number.startsWith( "$") || number.startsWith( "#" ) )
		{
			index = 1;
		}
		
		// 0 [x|X] [0..9|a..f|A..F]+
		
		else if( number.startsWith( "0x") || number.startsWith( "0X" ) )
		{
			index = 2;
		}

		// [x|X] [0..9|a..f|A..F]+ [h|H]

		else if( number.endsWith( "h") || number.endsWith( "H" ) )
		{
			lenght -= 1;
		}

		// --------------------------------------------------------------------

		for( int i = index; i < lenght ; i++ )
		{
			char c = number.charAt( i );
			switch( c )
			{
				case '_':
					break;

				case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
					value = (value * 16) + (c - '0');
					break;

				case 'a', 'b', 'c', 'd', 'e', 'f':
					value = (value * 16) + (10 + (c - 'a'));
					break;

				case 'A', 'B', 'C', 'D', 'E', 'F':
					value = (value * 16) + (10 + (c - 'A'));
					break;

				default:
					String text = String.format( "Invalid character '%c' for hexadecimal at position %d in string '%s' [%d,%d].",
						c, i, number, index, lenght );
					throw new NumberFormatException( text );
			}
		}

		// --------------------------------------------------------------------

		return value;
	}

	@SuppressWarnings("unused")
	private int asciiToDigit( char c )
	{
		return (c >= 'A') ? ((c & 0x000F) + 9) : (c & 0x000F);
	}
}
