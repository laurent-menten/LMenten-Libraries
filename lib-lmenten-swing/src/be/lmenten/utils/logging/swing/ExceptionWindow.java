package be.lmenten.utils.logging.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import be.lmenten.utils.lang.ExceptionUtils;

public class ExceptionWindow
	extends JDialog
{
	private static final long serialVersionUID = 1L;

	private final JTextArea textArea;
	private final JScrollPane scrollPane;
	
	public ExceptionWindow( Frame frame )
	{
		super( frame );	

		textArea = new JTextArea( 20, 40 );
		textArea.setEditable( false );

		scrollPane = new JScrollPane( textArea );
		scrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

		getContentPane().add( scrollPane, BorderLayout.CENTER );
		pack();

		setLocationRelativeTo( getParent() );
	}

	public static void show( Frame frame, Throwable t )
	{
		show( frame, t , null, null, null );
	}

	public static void show( Frame frame, Throwable t, String title )
	{
		show( frame, t , title, null, null );		
	}

	public static void show( Frame frame, Throwable t, String title, String message )
	{
		show( frame, t , title, message, null );
	}

	public static void show( Frame frame, Throwable t, String title, String message, Icon icon )
	{
		ExceptionWindow d = new ExceptionWindow( frame );
		d.setTitle( title );
		
		d.textArea.setText( ExceptionUtils.toStringBuilder( t ).toString() );

		d.setVisible( true );
	}
}

