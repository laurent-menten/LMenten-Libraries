package be.lmenten.utils.lo;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import lo.utils.Calc;
import lo.utils.Lo;

import java.io.Serializable;
import java.util.logging.Logger;

public abstract class CalcDocumentProcessor
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private final String fileName;

	// ========================================================================
	// =
	// ========================================================================

	public CalcDocumentProcessor( String fileName )
	{
		this.fileName = fileName;
	}

	// ========================================================================
	// =
	// ========================================================================

	public String getFileName()
	{
		return fileName;
	}

	// ========================================================================
	// =
	// ========================================================================

	public void process( XComponentLoader loader )
	{
		XSpreadsheetDocument doc = Calc.openDoc( fileName, loader );

		String [] sheets = Calc.getSheetNames( doc );
		for( String sheetName : sheets )
		{
			XSpreadsheet sheet = Calc.getSheet( doc, sheetName );

			var processor = getSheetProcessor( sheet, sheetName );
			if( processor != null )
			{
				LOG.info( "Processing sheet " + sheetName );

				// ----------------------------------------------------------------

				long start = System.currentTimeMillis();

				processor.process();

				long duration = System.currentTimeMillis() - start;

				// ----------------------------------------------------------------

				long millis = duration % 1000;
				long second = (duration / 1000) % 60;
				long minute = (duration / (1000 * 60)) % 60;
				long hour = (duration / (1000 * 60 * 60)) % 24;
				String time = String.format( "%02d:%02d:%02d.%d", hour, minute, second, millis );

				LOG.info( " > processing time : " + time );
			}
			else
			{
				LOG.info( "Ignoring sheet " + sheetName );
			}
		}

		Lo.closeDoc( doc );
	}

	protected abstract CalcSheetProcessor<?> getSheetProcessor( XSpreadsheet sheet, String sheetname );

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG =
			Logger.getLogger( CalcDocumentProcessor.class.getName() );
}
