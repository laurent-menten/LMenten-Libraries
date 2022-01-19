package be.lmenten.utils.logging.fx;

import java.time.Instant;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import be.lmenten.utils.logging.LogRegExPackageFilter;
import be.lmenten.utils.ui.FXUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class LogWindow
{
	public static final int DEFAULT_THESHOLD = 1000;

	// ------------------------------------------------------------------------
	// -
	// ------------------------------------------------------------------------

	private static final ObservableList<LogRecord> log
		= FXCollections.observableArrayList();

	// ------------------------------------------------------------------------

	private static int threshold = DEFAULT_THESHOLD;
	private static boolean checkThresholdEnabled = true;

	/*package*/ static Stage stage;

	private static TableView<LogRecord> tableView;

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 */
	public static void display()
	{
		if( stage != null )
		{
			stage.show();
			return;
		}

		// --------------------------------------------------------------------
		// - 
		// --------------------------------------------------------------------

		stage = new Stage();

		stage.setTitle( "Java logger" );

		// --------------------------------------------------------------------
		// - Configure TableView ----------------------------------------------
		// --------------------------------------------------------------------

		tableView = new TableView<>( log );
		tableView.setMinHeight( 800 );

		TableColumn<LogRecord, Long> colThread = new TableColumn<>( "Thread" );
		colThread.setSortable( false );
		colThread.setMinWidth( 55 );
		colThread.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.THREAD_ID ) );
		colThread.setCellFactory( new LogRecordCellFactory<>( LogRecordField.THREAD_ID ) );
		tableView.getColumns().add( colThread );
		
		TableColumn<LogRecord, Long> colSequence = new TableColumn<>( "Sequence" );
		colSequence.setSortable( false );
		colSequence.setMinWidth( 55 );
		colSequence.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.SEQUENCE_NUMBER ) );		
		colSequence.setCellFactory( new LogRecordCellFactory<>( LogRecordField.SEQUENCE_NUMBER ) );
		tableView.getColumns().add( colSequence );

		TableColumn<LogRecord, Instant> colTime = new TableColumn<>( "Time" );
		colTime.setSortable( false );
		colTime.setMinWidth( 215 );
		colTime.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.INSTANT ) );		
		colTime.setCellFactory( new LogRecordCellFactory<>( LogRecordField.INSTANT ) );
		tableView.getColumns().add( colTime );

		TableColumn<LogRecord, String> colClass = new TableColumn<>( "Class" );
		colClass.setSortable( false );
		colClass.setMinWidth( 300 );
		colClass.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.SOURCE_CLASS_NAME ) );		
		colClass.setCellFactory( new LogRecordCellFactory<>( LogRecordField.SOURCE_CLASS_NAME ) );
		tableView.getColumns().add( colClass );

		TableColumn<LogRecord, String> colMethod = new TableColumn<>( "Method" );
		colMethod.setSortable( false );
		colMethod.setMinWidth( 175 );
		colMethod.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.SOURCE_METHOD_NAME ) );		
		colMethod.setCellFactory( new LogRecordCellFactory<>( LogRecordField.SOURCE_METHOD_NAME ) );
		tableView.getColumns().add( colMethod );

		TableColumn<LogRecord, Level> colLevel = new TableColumn<>( "Level" );
		colLevel.setSortable( false );
		colLevel.setMinWidth( 70 );
		colLevel.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.LEVEL ) );		
		colLevel.setCellFactory( new LogRecordCellFactory<>( LogRecordField.LEVEL ) );
		tableView.getColumns().add( colLevel );

		TableColumn<LogRecord, String> colMessage = new TableColumn<>( "Message" );
		colMessage.setSortable( false );
		colMessage.setMinWidth( 450 );
		colMessage.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.MESSAGE ) );		
		colMessage.setCellFactory( new LogRecordCellFactory<>( LogRecordField.MESSAGE ) );
		tableView.getColumns().add( colMessage );

		TableColumn<LogRecord, Throwable> colException = new TableColumn<>( "Exception" );
		colException.setSortable( false );
		colException.setMinWidth( 300 );
		colException.setCellValueFactory( new LogRecordCellValueFactory<>( LogRecordField.THROWN ) );		
		colException.setCellFactory( new LogRecordCellFactory<>( LogRecordField.THROWN ) );
		tableView.getColumns().add( colException );
		
		// --------------------------------------------------------------------
		// -
		// --------------------------------------------------------------------

		Button clearButton = new Button( "Clear" );
		clearButton.setOnAction( ev ->
		{
			log.clear();

			tableView.refresh();
		} );

		// --------------------------------------------------------------------
		// -
		// --------------------------------------------------------------------

		HBox hbox = new HBox();
		hbox.setAlignment( Pos.CENTER );
		hbox.getChildren().add( clearButton );

		VBox vbox = new VBox();
		vbox.getChildren().addAll( tableView, hbox );

		// --------------------------------------------------------------------
		// -
		// --------------------------------------------------------------------

		Scene scene = new Scene( vbox );
		stage.setScene( scene );
		stage.show();

		// --------------------------------------------------------------------
		// - Install logging Handler ------------------------------------------
		// --------------------------------------------------------------------

		Logger.getLogger( "" ).addHandler( logHandler );
		LogRegExPackageFilter.reinstall();

		// --------------------------------------------------------------------
		// - Window on secondary screen ---------------------------------------
		// --------------------------------------------------------------------

		FXUtils.showOnSecondaryScreen( stage );
	}

	/**
	 * 
	 */
	public static void hide()
	{
		if( stage != null )
		{
			stage.hide();
		}
	}

	/**
	 * 
	 */
	public static void close()
	{
		if( stage != null )
		{
			stage.close();
			stage = null;
		}
	}

	// ========================================================================
	// =
	// ========================================================================

	public static Stage getStage()
	{
		return stage;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public static int getThreshold()
	{
		return threshold;
	}

	/**
	 * 
	 * @param threshold
	 */
	public static void setThreshold( int threshold )
	{
		LogWindow.threshold = threshold;

		checkThreshold();
	}

	/**
	 * 
	 */
	public static void resetThreshold()
	{
		setThreshold( DEFAULT_THESHOLD );
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static void checkThreshold()
	{
		if( checkThresholdEnabled && (log.size() > threshold) )
		{
			log.clear();
		
			tableView.refresh();
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param checkThresholdEnabled
	 */
	public static void setCheckThresholdEnabled( boolean checkThresholdEnabled )
	{
		LogWindow.checkThresholdEnabled = checkThresholdEnabled;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isCheckThresholdEnabled()
	{
		return checkThresholdEnabled;
	}

	// ========================================================================
	// = Logging Handler ======================================================
	// ========================================================================

	private static final Handler logHandler = new Handler()
	{
		@Override
		public void publish( LogRecord record )
		{
			if( this.isLoggable( record ) )
			{
				checkThreshold();

				log.add( record );

				Platform.runLater( () -> tableView.scrollTo( tableView.getItems().size()-1) );
			}
		}
		
		@Override
		public void flush()
		{
		}
		
		@Override
		public void close()
			throws SecurityException
		{
			LogWindow.close();
		}
	};
}
