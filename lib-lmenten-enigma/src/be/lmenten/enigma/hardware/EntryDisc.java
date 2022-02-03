package be.lmenten.enigma.hardware;

/**
 * <i>Eintrittswalze</i>
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public enum EntryDisc
	implements Enigma
{
	ALPHA		( "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ),
	QWERTZ		( "QWERTZUIOASDFGHJKPYXCVBNML" ),
	;

	// ------------------------------------------------------------------------

	private final int forward [];

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	/**
	 * 
	 * @param forward
	 */
	private EntryDisc( String data )
	{
		assert( data.length() == getPositionsCount() ) : "Invalid EntryDisc mapping";

		this.forward = new int [ getPositionsCount() ];

		for( int i = 0 ; i < getPositionsCount() ; i++ )
		{
			this.forward[ charToPos( data.charAt(i) ) ] = i;
		}
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param c
	 * @return
	 */
	public int forward( char c )
	{
		return forward( charToPos( c ) );
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public int forward( int pos )
	{
		return forward[ pos ];
	}
}
