package be.lmenten.utils.net;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class URILoggerProxySelector
	extends ProxySelector
{
	private static final boolean DEFAULT_ENABLED_STATE = false;

	private final ProxySelector defaultProxySelector;
	private final List<URI> requests = new ArrayList<>();

	private boolean enabled;

	// ========================================================================
	// =
	// ========================================================================

	public URILoggerProxySelector()
	{
		this( ProxySelector.getDefault(), DEFAULT_ENABLED_STATE );
	}

	public URILoggerProxySelector( boolean enabled )
	{
		this( ProxySelector.getDefault(), enabled );
	}

	public URILoggerProxySelector( ProxySelector defaultProxySelector )
	{
		this( defaultProxySelector, DEFAULT_ENABLED_STATE );
	}

	public URILoggerProxySelector( ProxySelector defaultProxySelector, boolean enabled )
	{
		this.defaultProxySelector = defaultProxySelector;
		this.enabled = enabled;
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 *
 	 * @param enabled
	 */
	public void setEnabled( boolean enabled )
	{
		this.enabled = enabled;
	}

	/**
	 *
	 * @return
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	// ------------------------------------------------------------------------

	/**
	 *
	 * @return
	 */
	public Stream<URI> stream()
	{
		return requests.stream();
	}

	/**
	 *
	 * @return the list of requested URIs.
	 */
	public List<URI> toList()
	{
		return requests.stream().toList();
	}

	/**
	 *
	 * @param predicate
	 * @return a filtered list of requested URIs.
	 */
	public List<URI> filterList( Predicate<URI> predicate )
	{
		return requests.stream().filter( predicate ).toList();
	}

	/**
	 *
	 * @return
	 */
	public int size()
	{
		return requests.size();
	}

	/**
	 * Empty the list of requested URIs.
	 */
	public void clear()
	{
		requests.clear();
	}

	// ========================================================================
	// = ProxySelector interface ==============================================
	// ========================================================================

	@Override
	public List<Proxy> select( URI uri )
	{
		requests.add( uri );

		return defaultProxySelector.select( uri );
	}

	@Override
	public void connectFailed( URI uri, SocketAddress sa, IOException ioe )
	{
		defaultProxySelector.connectFailed( uri, sa, ioe );
	}
}
