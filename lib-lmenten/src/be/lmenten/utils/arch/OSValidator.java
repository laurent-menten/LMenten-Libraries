package be.lmenten.utils.arch;

public class OSValidator
{	 
	private static String OS = System.getProperty( "os.name" ).toLowerCase();

	public static boolean isUnix()
	{
		return (OS.contains( "nix" )
				|| OS.contains( "nux" )
				|| OS.contains( "aix" ) );
	}

	public static boolean isWindows()
	{
		return OS.contains( "win" );
	}

	public static boolean isMac()
	{
		return OS.contains( "mac" );
	}

	public static boolean isSolaris()
	{
		return OS.contains( "sunos" );
	}

	public static OSFlavour getOSFlavour()
	{
		if( isUnix() )			return OSFlavour.UNIX;
		else if( isWindows() )	return OSFlavour.WINDOWS;
		else if( isMac() )		return OSFlavour.MACOS;
		else					return OSFlavour.UNKNWON;
    }
}