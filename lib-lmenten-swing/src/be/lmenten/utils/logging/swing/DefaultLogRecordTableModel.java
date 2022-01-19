package be.lmenten.utils.logging.swing;

import java.util.logging.LogRecord;

public class DefaultLogRecordTableModel
	extends AbstractLogRecordTableModel
{
	private static final long serialVersionUID = 1L;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultLogRecordTableModel()
	{
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void addRecord( LogRecord record )
	{
		records.add( record );

		fireTableDataChanged();
	}
}
