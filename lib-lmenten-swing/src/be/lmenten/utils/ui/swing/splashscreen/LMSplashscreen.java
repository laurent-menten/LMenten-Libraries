package be.lmenten.utils.ui.swing.splashscreen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JWindow;

public abstract class LMSplashscreen<T, U>
	extends JWindow
{
	private static final long serialVersionUID = 1L;

	// =========================================================================
	// === Constructor =========================================================
	// =========================================================================

	public LMSplashscreen()
	{
		setBackground( new Color( 0, 0, 0, 0 ) );
		setAlwaysOnTop( true );
		setVisible( true );
	}

	// =========================================================================
	// === Message from app ====================================================
	// =========================================================================

	public void message( T progress, U message )
	{
	}
	
	// =========================================================================
	// === Visual refresh ======================================================
	// =========================================================================

	public void paint( Graphics g )
	{
		super.paint( g );
	}	
}