package be.lmenten.utils.logging.swing;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractLogRecordTableModel
	extends AbstractTableModel
	implements LogRecordTableModel
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private static final int COL_THREAD = 0;
	private static final int COL_SEQUENCE = 1;
	private static final int COL_INSTANT = 2;
	private static final int COL_CLASS = 3;
	private static final int COL_METHOD = 4;
	private static final int COL_LEVEL = 5;
	private static final int COL_MESSAGE = 6;
	private static final int COL_PARAMETERS = 7;
	private static final int COL_EXCEPTION = 8;

	private static final String [] columns = 
	{
		"Thread",			// 0 
		"#",				// 1 
		"Time",				// 2 
		"Class",			// 3 
		"Method",			// 4 
		"Level",			// 5 
		"Message",			// 6 
		"Parameters",		// 7 
		"Exception",		// 8 
	};

	private static final Class<?> [] types = 
	{
		Integer.class,		// 0 thread id
		Integer.class,		// 1 message #
		Instant.class,		// 2 time
		String.class,		// 3 class
		String.class,		// 4 method
		Level.class,		// 5 level
		String.class,		// 6 message
		Boolean.class,		// 7 parameters
		String.class		// 8 exception
	};

	private static final int [] widths =
	{
		55,					// 0 thread id
		55,					// 1 message #
		215,				// 2 time
		300,				// 3 class
		175,				// 4 method
		70,					// 5 level
		450,				// 6 message
		80,					// 7 parameters
		300					// 8 exception
	};

	// ------------------------------------------------------------------------

	protected List<LogRecord> records;

	// ========================================================================
	// ===
	// ========================================================================

	public AbstractLogRecordTableModel()
	{
		records = new ArrayList<>();
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getColumnCount()
	{
		return columns.length;
	}

	@Override
	public String getColumnName( int columnIndex )
	{
		return columns[ columnIndex ];
	}

	@Override
	public Class<?> getColumnClass( int columnIndex )
	{
		return types[ columnIndex ];
	}

	@Override
	public int getColumnWidth( int columnIndex )
	{
		return widths[ columnIndex ];
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getRowCount()
	{
		return records.size();
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		LogRecord record = records.get( rowIndex );

		switch( columnIndex )
		{
			case COL_THREAD:
				return record.getLongThreadID();

			case COL_SEQUENCE:
				return record.getSequenceNumber();

			case COL_INSTANT:
				return record.getInstant();

			case COL_CLASS:
				return record.getSourceClassName();

			case COL_METHOD:
				return record.getSourceMethodName();

			case COL_LEVEL:
				return record.getLevel();

			case COL_MESSAGE:
				return record.getMessage();

			case COL_PARAMETERS:
				return (record.getParameters() == null);

			case COL_EXCEPTION:
				return record.getThrown() == null ? null : record.getThrown().toString();

			default:
		}

		return null;
	}
}
