package be.lmenten.utils.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class FXUtils
{
	// ========================================================================
	// = Helpers for multiple screens =========================================
	// ========================================================================

	/**
	 * 
	 * @param stage
	 */
	public static void showOnPrimaryScreen( Stage stage )
	{
		showOnScreen( stage, Screen.getPrimary() );		
	}

	/**
	 * 
	 * @param stage
	 */
	public static void showOnSecondaryScreen( Stage stage )
	{
		ObservableList<Screen> screens = Screen.getScreens();
		screens.forEach( screen ->
		{
			if( screen != Screen.getPrimary() )
			{
				showOnScreen( stage, screen );
				return;
			}
		} );
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param stage
	 * @param screen
	 */
	public static void showOnScreen( Stage stage, Screen screen )
	{
		Rectangle2D bounds = screen.getBounds();
        stage.setX(bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY(bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2);
	}

	// ========================================================================
	// = Helpers for node styles ==============================================
	// ========================================================================

	/**
	 * 
	 * @param node
	 * @param name
	 * @param value
	 */
	public static void addStyle( Node node, String name, String value )
	{
		String styles = node.getStyle();

		styles += name + ":" + value + ";";

		node.setStyle( styles );
	}

	/**
	 * 
	 * @param node
	 * @param name
	 * @param value
	 */
	public static void updateStyle( Node node, String name, String value )
	{
		String styles = node.getStyle();

		int index = styles.indexOf( name );
		if( index != -1 )
		{
			index = styles.indexOf( ":", index );
			
			int index2 = styles.indexOf( ";", index );

			String before = styles.substring( 0, index+1 );
			String after = styles.substring( index2 );

			styles = before + value + after;
		}
		else
		{
			styles += name + ":" + value + ";";
		}

		node.setStyle( styles );
	}

	/**
	 * 
	 * @param node
	 * @param name
	 */
	public static void removeStyle( Node node, String name )
	{
		String styles = node.getStyle();

		styles = styles.replaceAll( name + ":[^;]*;", "" );

		node.setStyle( styles );		
	}
}
