package be.lmenten.enigma.hardware;

public enum Keyboard
{
	SWEDEN	( "ABCDEFGHIJKLMNOPQRSTUVXYZ���" ),

	ALPHA	( "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ),

	QWERTZ	( "QWERTZUIOASDFGHJKPYXCVBNML" ),

	NUMERIC	( "0123456789" ),

	;

	//

	private final String data;
	private final int keyboardSize;

	// ========================================================================
	// = 
	// ========================================================================

	private Keyboard( String data )
	{
		this.data = data;
		this.keyboardSize = data.length();
	}

	// ========================================================================
	// = 
	// ========================================================================

	public int getKeyboardSize()
	{
		return keyboardSize;
	}

	public String getKeyboard()
	{
		return data;
	}

	public int charToPos( char c )
	{		
		return c - 'A';
	}

	public char posToChar( int pos )
	{
		return (char)('A' + pos);
	}
}
