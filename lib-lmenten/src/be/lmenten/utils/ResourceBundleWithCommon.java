package be.lmenten.utils;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

public class ResourceBundleWithCommon
	extends ResourceBundle
{
	private static String commonSuffix;

	private ResourceBundle res0;
	private ResourceBundle res;
	
	// ========================================================================
	// = 
	// ========================================================================

	public ResourceBundleWithCommon()
	{
		if( ResourceBundleWithCommon.commonSuffix == null )
		{
			throw new RuntimeException( "No baseName set in ResourceBundleWithCommon" );
		}
	}

	public static void setCommonBaseName( String commonSuffix )
	{
		ResourceBundleWithCommon.commonSuffix = commonSuffix;
	}

	// ========================================================================
	// = 
	// ========================================================================

	public static ResourceBundleWithCommon getBundleEx( String baseName )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ) );
		rb.res = ResourceBundle.getBundle( baseName );

		return rb;
	}

	public static ResourceBundleWithCommon getBundleEx( String baseName, ResourceBundle.Control control )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), control );
		rb.res = ResourceBundle.getBundle( baseName, control );
		return rb;
	}

	// ------------------------------------------------------------------------

	public static ResourceBundleWithCommon getBundleEx( String baseName, Locale locale )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), locale );
		rb.res = ResourceBundle.getBundle( baseName, locale );
		return rb;
	}

	public static ResourceBundleWithCommon getBundleEx( String baseName, Locale targetLocale, ResourceBundle.Control control )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), targetLocale, control );
		rb.res = ResourceBundle.getBundle( baseName, targetLocale, control );
		return rb;
	}

	// ------------------------------------------------------------------------

	public static ResourceBundleWithCommon getBundleEx( String baseName, Locale targetLocale, ClassLoader loader )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), targetLocale, loader );
		rb.res = ResourceBundle.getBundle( baseName, targetLocale, loader );
		return rb;
	}

	public static ResourceBundleWithCommon getBundleEx( String baseName, Locale targetLocale, ClassLoader loader, ResourceBundle.Control control )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), targetLocale, loader, control );
		rb.res = ResourceBundle.getBundle( baseName, targetLocale, loader, control );
		return rb;
	}

	// ------------------------------------------------------------------------

	public static ResourceBundleWithCommon getBundle( String baseName, Module module )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), module );
		rb.res = ResourceBundle.getBundle( baseName, module );
		return rb;
	}

	public static final ResourceBundleWithCommon getBundle( String baseName, Locale targetLocale, Module module )
	{
		ResourceBundleWithCommon rb = new ResourceBundleWithCommon();
		
		rb.res0 = ResourceBundle.getBundle( getCommonBaseBundleName( baseName ), targetLocale, module );
		rb.res = ResourceBundle.getBundle( baseName, targetLocale, module );
		return rb;
	}

	// ========================================================================
	// = 
	// ========================================================================

	@Override
	public String getBaseBundleName()
	{
		return res.getBaseBundleName();
	}

	public String getCommonBaseBundleName()
	{
		return getCommonBaseBundleName( res.getBaseBundleName() );
	}

	public static String getCommonSuffix()
	{
		return ResourceBundleWithCommon.commonSuffix;
	}

	private static String getCommonBaseBundleName( String basename )
	{
		return basename + "-" + ResourceBundleWithCommon.commonSuffix;
	}

	@Override
	public Locale getLocale()
	{
		return res.getLocale();
	}

	// ========================================================================
	// = 
	// ========================================================================

	public boolean containsKey( String key )
	{
		return res.containsKey( key )
				|| res0.containsKey( key );
	}

	public Enumeration<String> getKeys()
	{
		Vector<String> v = new Vector<>();
		

		Enumeration<String> r = res.getKeys();
		while( r.hasMoreElements() )
		{
			v.add( r.nextElement() );
		}

		Enumeration<String> r0 = res0.getKeys();
		while( r0.hasMoreElements() )
		{
			String s = r0.nextElement();
			if( ! v.contains( s ) )
			{
				v.add( s );
			}
		}
		
		return v.elements();
	}

	public Set<String> keySet()
	{
		HashSet<String> r = new HashSet<>( res0.keySet() );

		r.addAll( res.keySet() );

		return r;
	}

	// ----------------------------------------------------------------

	@Override
	protected Object handleGetObject( String key )
	{
		try
		{
			return res.getObject( key );
		}
		catch (  MissingResourceException ex )
		{
			return res0.getObject( key );
		}
	}
}
