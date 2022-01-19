package be.lmenten.utils.logging.swing;

import java.util.logging.LogRecord;

import javax.swing.table.TableModel;

public interface LogRecordTableModel
	extends TableModel
{
	public int getColumnWidth( int columnIndex );

	public void addRecord( LogRecord record );
}
