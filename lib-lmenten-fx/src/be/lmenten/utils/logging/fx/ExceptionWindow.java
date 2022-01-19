package be.lmenten.utils.logging.fx;

import be.lmenten.utils.lang.ExceptionUtils;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class ExceptionWindow
{
	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param ex
	 */
	public static void display( Throwable ex )
	{
		display( null, ex );
	}

	/**
	 * 
	 * @param parent
	 * @param ex
	 */
	public static void display( Window parent, Throwable ex )
	{
		Stage stage = new Stage();
		stage.setTitle( ex.getClass().getSimpleName() );

		// --------------------------------------------------------------------
		// - 
		// --------------------------------------------------------------------

		double width = 800;
		double height = 600;

		if( parent == null )
		{
			stage.initModality( Modality.APPLICATION_MODAL );
		}
		else
		{
			stage.initOwner( parent );
			stage.initModality( Modality.WINDOW_MODAL );
		}

		// --------------------------------------------------------------------
		// - 
		// --------------------------------------------------------------------

		
		TextArea textArea = new TextArea();
		textArea.setMinWidth( width );
		textArea.setMinHeight( height );
		
		textArea.setText( ExceptionUtils.toStringBuilder( ex ).toString() );

		// --------------------------------------------------------------------
		// - 
		// --------------------------------------------------------------------

		Scene scene = new Scene( textArea );
		stage.setScene( scene );
		stage.showAndWait();
	}
}
