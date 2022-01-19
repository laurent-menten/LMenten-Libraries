package be.lmenten.utils.ui.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileImporter
{
	// ========================================================================
	// = 
	// ========================================================================

	public FileImporter()
	{
	}

	// ========================================================================
	// = 
	// ========================================================================

	public File chooseFile()
	{
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle( "Choose an iHex program file" );
		fc.setCurrentDirectory( new File( "." ) );
        fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter( new FileFilter()
		{
			public String getDescription()
			{
		        return "Excel file (*.xlsx)";
		    }

		    public boolean accept( File f )
		    {
		    	if( f.isDirectory() )
		    	{
		    		return true;
		        }
		    	else
		    	{
		            return f.getName().toLowerCase().endsWith( ".xlsx" );
		        }
		    }
		} );

		int returnVal = fc.showOpenDialog( null );
		if( returnVal == JFileChooser.APPROVE_OPTION )
		{
				return fc.getSelectedFile();
		}

		return null;
	}
}
