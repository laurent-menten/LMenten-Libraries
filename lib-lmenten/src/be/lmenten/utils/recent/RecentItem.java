package be.lmenten.utils.recent;

public class RecentItem
	implements Comparable<RecentItem>
{
	private String name;
	private String path;

	// ========================================================================
	// = 
	// ========================================================================

	public RecentItem( String name, String path )
	{
		this.name = name;
		this.path = path;
	}

	// ========================================================================
	// = 
	// ========================================================================

	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		return path;
	}

	// ========================================================================
	// = 
	// ========================================================================

	@Override
	public int compareTo( RecentItem o )
	{
		int r = getName().compareTo( o.getName() );
		if( r != 0 )
		{
			return r;
		}
		
		return getPath().compareTo( o.getPath() );
	}

	@Override
	public boolean equals( Object obj )
	{
		return compareTo( (RecentItem) obj ) == 0;
	}

	// ========================================================================
	// = 
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( "RecentItem{ \"" )
		 .append( name )
		 .append( "\", \"" )
		 .append( path )
		 .append( "\" }" )
		 ;

		return s.toString();
	}
}
