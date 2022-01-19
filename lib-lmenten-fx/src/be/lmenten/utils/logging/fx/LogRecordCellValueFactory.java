package be.lmenten.utils.logging.fx;

import java.text.MessageFormat;
import java.util.logging.LogRecord;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 08 / 25
 */
public class LogRecordCellValueFactory<T>
	implements Callback<CellDataFeatures<LogRecord, T>, ObservableValue<T>>
{
	private final LogRecordField field;

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param field
	 */
	public LogRecordCellValueFactory( LogRecordField field )
	{
		this.field = field;
	}

	// ========================================================================
	// = 
	// ========================================================================

	@Override
	@SuppressWarnings("unchecked")
	public ObservableValue<T> call( CellDataFeatures<LogRecord, T> p )
	{
		switch( field )
		{
			case THREAD_ID:
				return (ObservableValue<T>) new SimpleLongProperty( p.getValue().getLongThreadID() ).asObject();

			case SEQUENCE_NUMBER:
				return (ObservableValue<T>) new SimpleLongProperty( p.getValue().getSequenceNumber() ).asObject();

			case INSTANT:
				return (ObservableValue<T>) new SimpleObjectProperty<>( p.getValue().getInstant() );

			case SOURCE_CLASS_NAME:
				return (ObservableValue<T>) new SimpleStringProperty( p.getValue().getSourceClassName() );

			case SOURCE_METHOD_NAME:
				return (ObservableValue<T>) new SimpleStringProperty( p.getValue().getSourceMethodName() );

			case LEVEL:
				return (ObservableValue<T>) new SimpleObjectProperty<>( p.getValue().getLevel() );

			case MESSAGE:
		    	String message = p.getValue().getMessage();

		    	Object [] parameters =  p.getValue().getParameters();
		    	if( (parameters != null) && (parameters.length > 0) )
		    	{
		            message = MessageFormat.format( message, parameters );
		    	}

		    	return (ObservableValue<T>) new SimpleStringProperty( message );

			case THROWN:
				return (ObservableValue<T>) new SimpleObjectProperty<>( p.getValue().getThrown() );
		}

		throw new RuntimeException( "Unhandled LogRecord field type " + field );

	}
}
