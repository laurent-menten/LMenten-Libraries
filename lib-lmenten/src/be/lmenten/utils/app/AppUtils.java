package be.lmenten.utils.app;

public class AppUtils
{
	public static boolean runningFromJar( Class<?> clazz )
	{
		String protocol = clazz.getResource( "" ).getProtocol();
		if( "jar".equalsIgnoreCase( protocol ) )
		{
			return true;
		}
		else if( "file".equalsIgnoreCase( protocol ) )
		{
			return false;
		}

		throw new AssertionError( "Running from neither jar nor file ..." );
	}
}
