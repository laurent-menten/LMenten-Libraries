package be.lmenten.utils.lo;

import com.sun.star.sheet.XCellRangesQuery;
import com.sun.star.sheet.XSheetCellRanges;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCellRange;
import lo.utils.Calc;
import lo.utils.Lo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class CalcSheetProcessor<T>
{
	static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

	// ------------------------------------------------------------------------

	protected final XSpreadsheet sheet;
	protected final List<T> data;

	protected final CellRangeAddress[] addresses;

	protected final int colCount;
	protected final String colNames;
	protected final int rowCount;

	// ========================================================================
	// =
	// ========================================================================

	public CalcSheetProcessor( XSpreadsheet sheet, List<T> data )
	{
		this.sheet = sheet;
		this.data = data;

		// --------------------------------------------------------------------

		XCellRange cellRange = Calc.findUsedRange( sheet );
		XCellRangesQuery query = Lo.qi( XCellRangesQuery.class, cellRange );
		XSheetCellRanges ranges = query.queryVisibleCells();
		addresses = ranges.getRangeAddresses();

		colCount = addresses[0].EndColumn - addresses[0].StartColumn + 1;
		colNames = LoSheetHelper.getColNamesList( sheet, addresses[0].StartRow, colCount );
		rowCount = (addresses[0].EndRow - addresses[0].StartRow);
	}

	public void process()
	{
		preprocess();

		processFirstRow( addresses[0].StartRow );

		for( int i = addresses[0].StartRow + 1; i <= addresses[0].EndRow ; i++ )
		{
			processRow( i );
		}
	}

	public void preprocess()
	{
	}

	public void processFirstRow( int rowNumber )
	{
	}

	public abstract void processRow( int rowNumber );

	// =========================================================================
	// ===
	// =========================================================================

	public String getColNamesList( int row, int colCount )
	{
		return LoSheetHelper.getColNamesList( sheet, row, colCount );
	}

	// ------------------------------------------------------------------------

	public LocalDate getDate( int column, int row )
	{
		return LoSheetHelper.getDate( sheet, column, row );
	}

	public Double getDouble( int column, int row )
	{
		return LoSheetHelper.getDouble( sheet, column, row );
	}

	public Integer getInteger( int column, int row )
	{
		return LoSheetHelper.getInteger( sheet, column, row );
	}

	public String getString( int column, int row )
	{
		return LoSheetHelper.getString( sheet, column, row );
	}
}
