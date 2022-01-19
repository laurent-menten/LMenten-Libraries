package be.lmenten.utils.lo;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.CellContentType;
import com.sun.star.table.XCell;

import lo.utils.Calc;

public interface LoSheetHelper
{
	static final DateTimeFormatter dtFormat
		= DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

	// =========================================================================

	public static String getColNamesList( XSpreadsheet sheet, int row, int colCount )
	{
		StringBuilder s = new StringBuilder();

		s.append( (String) Calc.getString( sheet, 0, 0 ) );

		for( int i = 1 ; i < colCount ; i++ )
		{
			s.append( "," )
			 .append( (String) Calc.getString( sheet, i, 0 ) )
			 ;
		}

		return s.toString();
	}
	
	// =========================================================================
	// ===
	// =========================================================================

	public static LocalDate getDate( XSpreadsheet sheet, int column, int row )
	{
		LocalDate dayDate = null;

		XCell cell = Calc.getCell( sheet, column, row );

		if( cell.getType() == CellContentType.VALUE )
		{
			Double dateRaw = (Double) Calc.getVal( sheet, column, row );
			if( dateRaw.intValue() != 0 )
			{
				dayDate = LocalDate.ofEpochDay( dateRaw.intValue() - 25569 );
			}
		}

		else if( cell.getType() == CellContentType.TEXT )
		{
			String dateRaw = (String) Calc.getString( sheet, column, row  );
			if( dateRaw != null )
			{
				if( dateRaw.startsWith( "'" ) )
				{
					dateRaw = dateRaw.substring( 1 );
				}

				dayDate = LocalDate.parse( dateRaw, dtFormat );
			}
		}

		return dayDate;
	}

	// ------------------------------------------------------------------------

	public static Double getDouble( XSpreadsheet sheet, int column, int row )
	{
		Double value = null;

		XCell cell = Calc.getCell( sheet, column, row );

		if( cell.getType() == CellContentType.VALUE )
		{
			Double dataRaw = (Double) Calc.getVal( sheet, column, row );
			
			value = dataRaw.doubleValue();
		}

		else if( cell.getType() == CellContentType.TEXT )
		{
			String dataRaw = (String) Calc.getString( sheet, column, row  );
			if( dataRaw != null )
			{
				if( dataRaw.startsWith( "'" ) )
				{
					dataRaw = dataRaw.substring( 1 );
				}

				value = Double.parseDouble( dataRaw );
			}
		}

		return value;
	}

	// ------------------------------------------------------------------------

	public static Integer getInteger( XSpreadsheet sheet, int column, int row )
	{
		Integer value= null;

		XCell cell = Calc.getCell( sheet, column, row );

		if( cell.getType() == CellContentType.VALUE )
		{
			Double dataRaw = (Double) Calc.getVal( sheet, column, row );
			
			value = dataRaw.intValue();
		}

		else if( cell.getType() == CellContentType.TEXT )
		{
			String dataRaw = (String) Calc.getString( sheet, column, row  );
			if( dataRaw != null )
			{
				if( dataRaw.startsWith( "'" ) )
				{
					dataRaw = dataRaw.substring( 1 );
				}

				value = Integer.parseInt( dataRaw );
			}
		}

		return value;
	}

	// ------------------------------------------------------------------------

	public static String getString( XSpreadsheet sheet, int column, int row )
	{
		return (String) Calc.getString( sheet, column, row );
	}
}
