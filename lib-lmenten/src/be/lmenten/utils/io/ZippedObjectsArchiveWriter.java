package be.lmenten.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZippedObjectsArchiveWriter
	implements AutoCloseable
{
	private final FileOutputStream fos;
	private final ZipOutputStream zos;
	private final ByteArrayOutputStream baos;
	private final ObjectOutputStream oos;

	// ========================================================================
	// = 
	// ========================================================================

	public ZippedObjectsArchiveWriter( String filename )
		throws FileNotFoundException, IOException
	{
		this( new File( filename ) );
	}

	public ZippedObjectsArchiveWriter( File file )
		throws FileNotFoundException, IOException
	{
		fos = new FileOutputStream( file );
		zos = new ZipOutputStream( fos );

		baos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream( baos );
	}

	// ========================================================================
	// = 
	// ========================================================================

	public void writeObject( String objectName, Object object )
		throws IOException
	{
		ZipEntry ze = new ZipEntry( objectName );
		zos.putNextEntry( ze );

		oos.writeObject( object );
		oos.flush();
		byte [] data = baos.toByteArray();
		baos.reset();

		zos.write( data, 0, data.length );
		zos.closeEntry();
	}

	// ------------------------------------------------------------------------

	public interface ObjectWriter
	{
		public void write( ObjectOutputStream oos )
			throws IOException;
	};

	public void writeObjects( String objectName, ObjectWriter ow )
		throws IOException
	{
		ZipEntry ze = new ZipEntry( objectName );
		zos.putNextEntry( ze );

		ow.write( oos );
		oos.flush();
		byte [] data = baos.toByteArray();
		baos.reset();

		zos.write( data, 0, data.length );
		zos.closeEntry();
	}

	// ========================================================================
	// = 
	// ========================================================================
	
	@Override
	public void close()
		throws IOException
	{
		if( zos != null )
		{
			zos.close();
		}

		if( oos != null )
		{
			oos.close();
		}
	}
}
