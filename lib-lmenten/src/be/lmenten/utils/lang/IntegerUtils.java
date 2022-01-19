package be.lmenten.utils.lang;

/*
 * 		byte	1
 * 		short	2
 * 		int		4
 * 		long	8
 */

public class IntegerUtils
{
	public static long composeLong( byte byte0 )
	{
		long v = 0;

		if( (byte0 & 0b1000_0000) == 0b1000_0000 )
		{
			v = (-1 << 8);
		}

		return v | (byte0 & 0xFF);
	}

	public static long composeLong( byte byte1, byte byte0 )
	{
		long v = 0;

		if( (byte1 & 0b1000_0000) == 0b1000_0000 )
		{
			v = (-1 << 16);
		}

		return v | ((byte1 << 8) & (0xFF << 8)) | (byte0 & 0xFF);
	}

	public static long composeLong( byte byte3, byte byte2, byte byte1, byte byte0 )
	{
		long v = 0;

		if( (byte3 & 0b1000_0000) == 0b1000_0000 )
		{
			v = (-1 << 24);
		}

		return v | ((byte3 << 24) & (0xFF << 24))
				 | ((byte2 << 16) & (0xFF << 16))
				 | ((byte1 << 8) & (0xFF << 8))
				 | (byte0 & 0xFF);
	}
}
