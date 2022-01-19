package be.lmenten.utils.logging.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//import be.lmenten.app.LMApplication;
//import net.java.linoleum.jlfgr.Jlfgr;

public class JLogHandlerFrame
	extends JFrame
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private boolean closed;

	private JTable logTable;
	private JScrollPane scrollPane;

	private LogRecordTableModel model;

	// ========================================================================
	// ===
	// ========================================================================

	public JLogHandlerFrame( String appName )
	{
		setTitle( "Logging window - " + appName );
//		setIconImage( Jlfgr.DEV_HOST.getImage16() );

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );

		this.closed = false;
		addWindowListener( new WindowAdapter()
		{
			private static final String TXT_CLOSE_MESSAGE
			=	"<html>"
			+		"Do you really want to close log window?"
			+	"</html>"
			;

			@Override
			public void windowClosing( WindowEvent ev )
			{
				// Source is set to application instance in LMApplication.close()
				// to distinguish programmatic close.

//				if( ev.getSource() instanceof LMApplication )
//				{
//					close();
//				}
//
//				// When user closes, ask for confirmation
//
//				else
//				{
					 int rc = JOptionPane.showConfirmDialog( null,
							 TXT_CLOSE_MESSAGE, "Log window",
							 JOptionPane.OK_CANCEL_OPTION );

					 if( rc == JOptionPane.OK_OPTION )
					 {
						 close();
					 }
//				}
			}
		} );
	
		model = new DefaultLogRecordTableModel();

		logTable = new JTable( model );
		logTable.setFillsViewportHeight( true );
		logTable.setDefaultRenderer( Level.class, new LevelRenderer() );

		int totalWidth = 0;
		logTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		for( int i = 0 ; i < model.getColumnCount() ; i++ )
		{
			int width = model.getColumnWidth( i );
			logTable.getColumnModel().getColumn( i ).setPreferredWidth( width );

			totalWidth += width + logTable.getIntercellSpacing().width;
		}

		scrollPane = new JScrollPane( logTable );

		getContentPane().add( scrollPane, BorderLayout.CENTER );

		Dimension d = new Dimension( totalWidth, 600 );
		setSize( d );
		setPreferredSize( d );
		pack();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public void close()
	{
		 setVisible( false );
		 dispose();

		 closed = true;		
	}

	public boolean isClosed()
	{
		return closed;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public Handler getHandler()
	{
		return logHandler;
	}

	public void installHandler()
	{
		Logger.getLogger( "" ).addHandler( logHandler );		
	}

	// ------------------------------------------------------------------------

	private final Handler logHandler = new Handler()
	{
		@Override
		public void publish( LogRecord record )
		{
			if( this.isLoggable( record ) )
			{
				JLogHandlerFrame.this.model.addRecord( record );

				showRow( logTable.getRowCount() - 1 );
			}
		}
		
		@Override
		public void flush()
		{
		}
		
		@Override
		public void close()
			throws SecurityException
		{
		}
	};

	private void showRow( int row )
	{
		Rectangle rect = logTable.getCellRect( row, 0, true );
		logTable.scrollRectToVisible(rect);

		logTable.clearSelection();
		logTable.setRowSelectionInterval( row, row );
//		((LogRecordTableModel)logTable.getModel()).fireTableDataChanged(); // notify the model
	}
}
