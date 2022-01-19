package be.lmenten.utils.logging.fx;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class LogRecordCellFactory<T>
	implements Callback<TableColumn<LogRecord, T>, TableCell<LogRecord, T>> 
{
	private final LogRecordField field;

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param field
	 */
	public LogRecordCellFactory( LogRecordField field )
	{
		this.field = field;
	}

	// ========================================================================
	// = 
	// ========================================================================

	private static final DateTimeFormatter DATETIME_FORMAT
		= DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" )
			.withZone( ZoneId.systemDefault() );


	@Override
	@SuppressWarnings("unchecked")
	public TableCell<LogRecord, T> call( TableColumn<LogRecord, T> p )
	{
		switch( field )
		{
			case THREAD_ID:
			case SEQUENCE_NUMBER:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,Long>()
				{
					@Override
					protected void updateItem( Long item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							setText( Long.toString( item ) );
						}

					}
				};

			case INSTANT:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,Instant>()
				{
					@Override
					protected void updateItem( Instant item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							String text = LogRecordCellFactory.DATETIME_FORMAT.format( item );
							setText( text );
						}

					}
				};

			case SOURCE_CLASS_NAME:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,String>()
				{
					@Override
					protected void updateItem( String item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							setText( item );
						}

					}
				};
				
			case SOURCE_METHOD_NAME:
			case MESSAGE:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,String>()
				{
					@Override
					protected void updateItem( String item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							setText( item );
						}

					}
				};

			case LEVEL:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,Level>()
				{
					@Override
					protected void updateItem( Level item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							setText( item.toString() );
							setStyle( "-fx-background-color: " + getLevelColor( item ) );
							setAlignment( Pos.CENTER );
						}

					}
				};

			case THROWN:
				return (TableCell<LogRecord, T>) new TableCell<LogRecord,Throwable>()
				{
					@Override
					protected void updateItem( Throwable item, boolean empty )
					{
						super.updateItem( item, empty );

						if( empty || item == null )
						{
					         setText( null );
					         setGraphic( null );
					     }
						else
						{
							setText( item.getClass().getName() );

							addEventFilter( MouseEvent.MOUSE_ENTERED, ev ->
							{
								TableCell<LogRecord, T> cell = (TableCell<LogRecord, T>) ev.getSource();

								cell.setText( "☛ " + item.getClass().getName() );
							} );

							addEventFilter( MouseEvent.MOUSE_EXITED, ev ->
							{
								TableCell<LogRecord, T> cell = (TableCell<LogRecord, T>) ev.getSource();

								cell.setText( item.getClass().getName() );
							} );

							addEventFilter( MouseEvent.MOUSE_CLICKED, ev -> 
							{
								if( ev.getClickCount() > 0 )
								{
									TableCell<LogRecord, T> cell = (TableCell<LogRecord, T>) ev.getSource();

									ExceptionWindow.display( LogWindow.stage, (Throwable)cell.getItem() );
								}
							} );
						}

					}
				};
		}

		throw new RuntimeException( "Unhandled LogRecord field type " + field );
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param level
	 * @return
	 */
	private String getLevelColor( Level level )
	{
		int levelIntValue = level.intValue();
		Color levelColor = Color.TRANSPARENT;

		if( levelIntValue >= Level.SEVERE.intValue() )
		{
			levelColor = Color.RED;
		}
		else if( levelIntValue >= Level.WARNING.intValue() )
		{
			levelColor = Color.ORANGE;
		}
		else if( levelIntValue >= Level.INFO.intValue() )
		{
			levelColor = Color.GREEN;
		}

		// ----------------------------------------------------------------

		else if( levelIntValue >= Level.CONFIG.intValue() )
		{
			levelColor = Color.LIGHTBLUE;
		}

		// ----------------------------------------------------------------

		else if( levelIntValue >= Level.FINE.intValue() )
		{
			levelColor = Color.DARKGRAY;
		}
		else if( levelIntValue >= Level.FINER.intValue() )
		{
			levelColor = Color.GREY;
		}
		else if( levelIntValue >= Level.FINEST.intValue() )
		{
			levelColor = Color.LIGHTGRAY;
		}

		return String.format("#%02X%02X%02X%02X",
                (int) (levelColor.getRed() * 255),
                (int) (levelColor.getGreen() * 255),
                (int) (levelColor.getBlue() * 255),
                (int) (0.33 * 255)
        );
	}
}
