package be.lmenten.utils.ui.swing;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class SwingUtils
{
	// ========================================================================
	// ===
	// ========================================================================

	public static void showOnScreen( int screen, Window frame )
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		GraphicsDevice graphicsDevice;

		if( (screen > -1) && (screen < gd.length) )
		{
			graphicsDevice = gd[screen];
		}
		else if( gd.length > 0 )
		{
			graphicsDevice = gd[0];
		}
		else
		{
			throw new RuntimeException( "No Screens Found" );
		}

		Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
		int screenWidth = graphicsDevice.getDisplayMode().getWidth();
		int screenHeight = graphicsDevice.getDisplayMode().getHeight();

		int x = bounds.x + (screenWidth - frame.getPreferredSize().width) / 2;
		int y = bounds.y + (screenHeight - frame.getPreferredSize().height) / 2;
		frame.setLocation( x, y );
		frame.setVisible( true );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public static void setNimbusLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel( NimbusLookAndFeel.class.getName() );
		}
		catch( ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			LOG.log( Level.WARNING, "Failed to set Nimbus L&F", e );
		}		
	}

	// ========================================================================
	// ===
	// ========================================================================

	public static Image getImage( String path )
	{
		BufferedImage buff = null;

		try
		{
			InputStream ins = SwingUtils.class.getResourceAsStream( path );
			buff = ImageIO.read( ins );
		}
		catch( Exception ex )
		{
			LOG.log( Level.WARNING, "Failed to load image \"" + path + "\"", ex );

			return null;
	    }

		return buff;
	}


	// ========================================================================
	// === LOGGING ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( SwingUtils.class.getName() );
}
