package be.lmenten.utils.logging.swing;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LevelRenderer
	extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private final Color defaultColor;

	private final Color colorSevere;
	private final Color colorWarning;
	private final Color colorInfo;

	private final Color colorConfig;

	private final Color colorFine;
	private final Color colorFiner;
	private final Color colorFinest;

	public LevelRenderer()
	{
		defaultColor = getBackground();

		colorSevere = new Color( 255, 51, 51 );
		colorWarning = new Color( 255, 153, 0 );
		colorInfo = new Color( 0, 255, 51);

		colorConfig = new Color( 51, 153, 255 );

		colorFine = new Color( 153, 153, 253 );
		colorFiner = new Color( 178, 178, 178 );
		colorFinest = new Color( 204, 204, 204 );
	}

	/**
	 *
	 */
	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
	{
		if( value instanceof Level )
		{
			Level level = (Level)value;

			if( level == Level.SEVERE )
			{
				setBackground( colorSevere );
			}
			else if( level == Level.WARNING )
			{
				setBackground( colorWarning );
			}
			else if( level == Level.INFO )
			{
				setBackground( colorInfo );
			}

			// ----------------------------------------------------------------

			else if( level == Level.CONFIG )
			{
				setBackground( colorConfig );
			}

			// ----------------------------------------------------------------

			else if( level == Level.FINE )
			{
				setBackground( colorFine );
			}
			else if( level == Level.FINER )
			{
				setBackground( colorFiner );
			}
			else if( level == Level.FINEST )
			{
				setBackground( colorFinest );
			}

			// ----------------------------------------------------------------

			else
			{
				setBackground( defaultColor );
			}
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
