package be.lmenten.utils.lang;

public class MathUtils
{
	public static int randomInt( int min, int max )
	{
		return (int) Math.floor( Math.random()*( max - min + 1) + min );
	}

	public static long randomLong( long min, long max )
	{
		return (long) Math.floor( Math.random()*( max - min + 1) + min );
	}

	public static double [] normalize( double ... list )
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for( int i = 0; i < list.length ; i++ )
		{
			if( list[i] < min ) min = list[i];
			if( list[i] > max ) max = list[i];
		}

		double [] normalized = new double [list.length];

		for( int i = 0; i < list.length ; i++ )
		{
			normalized[i] = (list[i] - min)/(max-min);
		}

		return normalized;
	}
}
