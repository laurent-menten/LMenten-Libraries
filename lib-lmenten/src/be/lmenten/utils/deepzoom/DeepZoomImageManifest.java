package be.lmenten.utils.deepzoom;

import be.lmenten.utils.io.FileUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class DeepZoomImageManifest
	extends DefaultHandler
{
	private final StringBuilder currentValue
		= new StringBuilder();

	private final String url;
	private final File cacheDir;

	private String format;
	private int overlap = 0;
	private int tileSize = 0;
	private int width = 0;
	private int height = 0;

	public DeepZoomImageManifest( String url, File cacheDir )
	{
		this.url = url;
		this.cacheDir = cacheDir;
	}

	public void process()
	{

		SAXParserFactory factory = SAXParserFactory.newInstance();

		try
		{
			SAXParser parser = factory.newSAXParser();

			try( InputStream is = new URL( url ).openStream() )
			{
				parser.parse( is, this );
			}
		}
		catch( ParserConfigurationException | SAXException | IOException ex )
		{
			ex.printStackTrace();
		}
	}

	// ========================================================================
	// = XML parser ===========================================================
	// ========================================================================

	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes )
		throws SAXException
	{
		currentValue.setLength(0);

		switch( qName )
		{
			case "Image":
			{
				format = attributes.getValue( "Format" );

				String overlap_raw = attributes.getValue( "Overlap" );
				overlap = Integer.parseInt( overlap_raw );

				String tileSize_raw = attributes.getValue( "TileSize" );
				tileSize = Integer.parseInt( tileSize_raw );

				break;
			}

			case "Size":
			{
				String width_raw = attributes.getValue( "Width" );
				width = Integer.parseInt( width_raw );

				String height_raw = attributes.getValue( "Height" );
				height = Integer.parseInt( height_raw );
				break;
			}
		}
	}

	@Override
	public void characters( char[] ch, int start, int length )
		throws SAXException
	{
		currentValue.append(ch, start, length);
	}

	// ========================================================================
	// =
	// ========================================================================

	public String getFormat()
	{
		return format;
	}

	public int getOverlap()
	{
		return overlap;
	}

	public int getTileSize()
	{
		return tileSize;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * Get the maximum zoom level for this tiled image.
	 *
	 * @return the maximum level
	 */
	public int getMaxLevel()
	{
		return (int) Math.ceil( Math.log( Math.max( width, height ) ) / Math.log( 2) );
	}

	/**
	 * Get the number of columns for the specified level.
	 *
	 * @param level the level
	 * @return the number of columns for the level
	 */
	public int getPyramidColumns( int level )
	{
		int leveledWidth = (width + 1) >> (getMaxLevel() - level);

		return (int) Math.ceil( leveledWidth / tileSize ) + 1;
	}

	/**
	 * Get the number of rows for the specified level.
	 *
	 * @param level the level
	 * @return the number of rows for the level
	 */
	public int getPyramidRows( int level )
	{
		int leveledHeight = (height + 1) >> (getMaxLevel() - level);

		return (int) Math.ceil( leveledHeight / tileSize ) + 1;
	}

	// ========================================================================
	// =
	// ========================================================================

	public String loadTile( int level, int column, int row ) throws IOException
	{
		File levelDir = new File( cacheDir, Integer.toString( level) );
		if( ! levelDir.exists() )
		{
			if( !levelDir.mkdirs() )
			{
				throw new IllegalStateException( "No cache directory for zoom level " + level + "." );
			}

			LOG.fine( "Created cache directory for zoom level " + level + "." );
		}

		File localFile = new File( cacheDir, getTileFilename( level, column, row ) );
		if( ! localFile.exists() )
		{
			FileUtils.retrieveFile( localFile.getCanonicalPath(), getTileUrl( level, column, row ) );
		}

		return localFile.getCanonicalPath();
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 *
	 * @param level
	 * @param column
	 * @param row
	 * @return
	 */
	public String getTileUrl( int level, int column, int row )
	{
		check( level, column, row );

		return String.format( "%s_files/%d/%d_%d.%s", url, level, column, row, format );
	}

	/**
	 *
	 * @param level
	 * @param column
	 * @param row
	 * @return
	 */
	public String getTileFilename( int level, int column, int row )
	{
		check( level, column, row );

		return String.format( "%d%s%d_%d.%s", level, File.separator, column, row, format );
	}

	// ------------------------------------------------------------------------

	private void check( int level, int column, int row )
	{
		if( level < 0 || level > getMaxLevel() )
		{
			throw new IllegalArgumentException( "Level not in [0," + getMaxLevel() + "[" );
		}

		if( column >= getPyramidColumns( level ) )
		{
			throw new IllegalArgumentException( "Column not in [0," + getPyramidColumns( level ) + "[" );
		}
		if( row >= getPyramidRows( level ) )
		{
			throw new IllegalArgumentException( "Row not in [0," + getPyramidRows( level ) + "[" );
		}
	}

	private static final Logger LOG
		= Logger.getLogger( DeepZoomImageManifest.class.getName() );
}
