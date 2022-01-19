package be.lmenten.utils.ui.awt;

import java.awt.Color;

public class AwtUtils
{
	public static String cssColor( Color color )
	{
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();

		return String.format( "#%2X%2X%2X%2X", r, g, b, a );
	}
}
