package be.lmenten.utils.lang;

public final class ThreadUtils
{
	public static void listTheads()
	{
		ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup parentGroup;
		while( (parentGroup = rootGroup.getParent()) != null )
		{
		    rootGroup = parentGroup;
		}
		
		Thread[] threads = new Thread[ rootGroup.activeCount() ];
		
		while( rootGroup.enumerate( threads, true ) == threads.length )
		{
			threads = new Thread [ threads.length * 2 ];
		}

		for( Thread thread : threads )
		{
			if( thread != null )
			{
				System.out.println( thread );
			}
		}		
	}
}
