package be.lmenten.utils.recent;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class RecentItems
{
	public final static String RECENT_ITEM_PREFIX = "recent.items.";
	public final static String RECENT_ITEM_NAME_SUFFIX = ".name";
	public final static String RECENT_ITEM_PATH_SUFFIX = ".path";

	private int	m_maxItems;
	private Preferences	m_prefNode;

	private List<RecentItem> m_items = new ArrayList<RecentItem>();
	private List<RecentItemsObserver> m_observers = new ArrayList<RecentItemsObserver>();

    // ========================================================================
    // = 
    // ========================================================================

	/**
     * 
     * @param maxItems
     * @param prefNode
     */
    public RecentItems( int maxItems, Preferences prefNode )
    {
        m_maxItems = maxItems;
        m_prefNode = prefNode;
        
        loadFromPreferences();
    }
    
    // ========================================================================
    // = 
    // ========================================================================

    public void push( RecentItem item )
    {
        m_items.remove( item );
        m_items.add( 0, item );
        
        if( m_items.size() > m_maxItems )
        {
            m_items.remove( m_items.size() - 1 );
        }
        
        update();
    }
    
    public void remove( Object item )
    {
        m_items.remove( item );

        update();
    }

    public void clear()
    {
    	m_items.clear();

    	update();
    }

    // ------------------------------------------------------------------------
    
    public RecentItem get( int index )
    {
        return (RecentItem)m_items.get( index );
    }
    
    public List<RecentItem> getItems()
    {
        return m_items;
    }
    
    public int size()
    {
        return m_items.size();
    }
    
    // ========================================================================
    // = 
    // ========================================================================

    public void addObserver( RecentItemsObserver observer )
    {
        m_observers.add( observer );
    }
    
    public void removeObserver( RecentItemsObserver observer )
    {
        m_observers.remove( observer );
    }
    
    private void update()
    {
        for( RecentItemsObserver observer : m_observers )
        {
            observer.onRecentItemChange( this );
        }
        
        storeToPreferences();
    }

    // ========================================================================
    // = 
    // ========================================================================

    /**
	 * 
	 */
	private void loadFromPreferences()
	{
		for( int i = 0 ; i < m_maxItems ; i++ )
		{
			String nameKey = RECENT_ITEM_PREFIX + i + RECENT_ITEM_NAME_SUFFIX;
			String pathKey = RECENT_ITEM_PREFIX + i + RECENT_ITEM_PATH_SUFFIX;

			String name = m_prefNode.get( nameKey, "" );
			if( !name.equals( "" ) )
			{
				String path = m_prefNode.get( pathKey, "" );

				RecentItem item = new RecentItem( name, path );
				m_items.add( item );
			}
			else
			{
				break;
			}
		}
	}

    /**
     * 
     */
    private void storeToPreferences()
    {
        for( int i = 0 ; i < m_maxItems ; i++ )
        {
			String nameKey = RECENT_ITEM_PREFIX + i + RECENT_ITEM_NAME_SUFFIX;
			String pathKey = RECENT_ITEM_PREFIX + i + RECENT_ITEM_PATH_SUFFIX;

			if( i < m_items.size() )
            {
                m_prefNode.put( nameKey, (String) m_items.get(i).getName() );
                m_prefNode.put( pathKey, (String) m_items.get(i).getPath() );
            }
            else
            {
                m_prefNode.remove( nameKey );
                m_prefNode.remove( pathKey );
            }
        }
    }
}

 