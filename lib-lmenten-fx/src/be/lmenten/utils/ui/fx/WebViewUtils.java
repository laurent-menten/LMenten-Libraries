package be.lmenten.utils.ui.fx;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebViewUtils
{
	public static boolean injectJQuery( WebEngine webEngine )
	{
		JSObject jQuery = null;

		try
		{
			jQuery = (JSObject) webEngine.executeScript( "$" );
		}
		catch( JSException ex )
		{
		}

		// --------------------------------------------------------------------

		if( jQuery == null )
		{
			try( InputStream is = WebViewUtils.class.getResourceAsStream( "jquery/jquery-3.6.0.min.js" );
				 InputStreamReader isr = new InputStreamReader( is );
				 BufferedReader br = new BufferedReader( isr ) )
			{
				StringBuilder jQueryContents = new StringBuilder();
				String line;
				while( (line = br.readLine()) != null )
				{
					jQueryContents.append( line );
				}

				webEngine.executeScript( jQueryContents.toString() );

				return true;
			}
			catch( IOException ex )
			{
				LOG.log( Level.SEVERE, "JQuery injection failed", ex );
			}
		}

		// --------------------------------------------------------------------

		return false;
	}

	public static List<Object> jQuerySelect( WebEngine webEngine, String selector )
	{
		List<Object> list = null;

		JSObject r = (JSObject) webEngine.executeScript( "$(\"" + selector + "\")" );
		Object rawLength = r.getMember( "length" );
		if( rawLength instanceof Number )
		{
			int length = ((Number) rawLength).intValue();

			list = new ArrayList<>(length);
			for( int i = 0; i < length; i++ )
			{
				list.add(r.getSlot(i));
			}
		}

		return list;
	}

















	/**
	 * Executes a script which may reference jQuery function on a document.
	 * Checks if the document loaded in a webEngine has a version of jQuery corresponding to
	 * the minimum required version loaded, and, if not, then loads jQuery into the document
	 * from the default JQUERY_LOCATION.
	 * @param engine the webView engine to be used.
	 * @Param jQueryLocation the location of the jQuery script to be executed.
	 * @param minVersion the minimum version of jQuery which needs to be included in the document.
	 * @param script provided javascript script string (which may include use of jQuery functions on the document).
	 * @return the result of the script execution.
	 */
	public static Object executejQuery( final WebEngine engine, String minVersion, String jQueryLocation, String script )
	{
		return engine.executeScript(
				"(function(window, document, version, callback) { "
						+ "var j, d;"
						+ "var loaded = false;"
						+ "if (!(j = window.jQuery) || version > j.fn.jquery || callback(j, loaded)) {"
						+ "  var script = document.createElement(\"script\");"
						+ "  script.type = \"text/javascript\";"
						+ "  script.src = \"" + jQueryLocation + "\";"
						+ "  script.onload = script.onreadystatechange = function() {"
						+ "    if (!loaded && (!(d = this.readyState) || d == \"loaded\" || d == \"complete\")) {"
						+ "      callback((j = window.jQuery).noConflict(1), loaded = true);"
						+ "      j(script).remove();"
						+ "    }"
						+ "  };"
						+ "  document.documentElement.childNodes[0].appendChild(script) "
						+ "} "
						+ "})(window, document, \"" + minVersion + "\", function($, jquery_loaded) {" + script + "});"
		);
	}

	// ========================================================================
	// = Utils ================================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( WebViewUtils.class.getName() );
}
