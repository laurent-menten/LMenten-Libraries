package be.lmenten.utils.ui.swing.splashscreen;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SimpleSplashscreen<T,U>
	extends LMSplashscreen<T,U>
{
	private static final long serialVersionUID = 1L;

	private ImageIcon imageIcon;
	private JLabel image;

	// =========================================================================
	// === Constructor =========================================================
	// =========================================================================

	public SimpleSplashscreen( String imageName )
	{
		setLayout(null);

		imageIcon = new ImageIcon( getClass().getResource( imageName ) );
		image = new JLabel( imageIcon );
		image.setBounds( 0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight() );
		add( image );
	
		setSize( imageIcon.getIconWidth(), imageIcon.getIconHeight() );

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width-getSize().width)/2;
		int y = (screenSize.height-getSize().height)/2;
		setLocation( x,y );
	}

	// =========================================================================
	// === LOGGING =============================================================
	// =========================================================================

//	private static final Logger LOG
//		= LMApplication.getLogger( SimpleSplashscreen.class );
}
