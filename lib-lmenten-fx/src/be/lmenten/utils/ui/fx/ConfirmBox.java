package be.lmenten.utils.ui.fx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox
{
	private static boolean returnValue;

	// ========================================================================
	// = 
	// ========================================================================

	public static boolean display( String title, String message )
	{
		Stage stage = new Stage();
		stage.initModality( Modality.APPLICATION_MODAL );
		stage.setMinWidth( 250 );
		stage.setTitle( title );

		// --------------------------------------------------------------------

		Label label = new Label();
		label.setText( message );

		Button yesButton = new Button( "Yes" );
		yesButton.setOnAction( ev ->
		{
			returnValue = true;
			stage.close();
		} );

		Button noButton = new Button( "No" );
		noButton.setOnAction( ev ->
		{
			returnValue = false;
			stage.close();
		} );
		
		// --------------------------------------------------------------------

		VBox layout = new VBox();
		layout.setAlignment( Pos.CENTER );
		layout.getChildren().addAll( label, yesButton, noButton );

		Scene scene = new Scene( layout );
		stage.setScene( scene );
		stage.showAndWait();

		// --------------------------------------------------------------------

		return returnValue;
	}
}
