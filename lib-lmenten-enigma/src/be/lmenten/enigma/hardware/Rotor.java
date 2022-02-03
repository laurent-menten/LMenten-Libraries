package be.lmenten.enigma.hardware;

/**
 * 
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public enum Rotor
	implements Enigma
{
	// ------------------------------------------------------------------------
	// - M3 (Kriegsmarine) ----------------------------------------------------
	// ------------------------------------------------------------------------

	I		( "EKMFLGDQVZNTOWYHXUSPAIBRCJ", Keyboard.ALPHA, 16 ),
	II		( "AJDKSIRUXBLHWTMCQGZNPYFVOE", Keyboard.ALPHA, 4 ),
	III		( "BDFHJLCPRTXVZNYEIWGAKMUSQO", Keyboard.ALPHA, 21 ),
	IV		( "ESOVPZJAYQUIRHXLNFTGKDCMWB", Keyboard.ALPHA, 9 ),
	V		( "VZBRGITYUPSDNHLXAWMJQOFECK", Keyboard.ALPHA, 25 ),
	VI		( "JPGVOUMFYQBENHZRDKASXLICTW", Keyboard.ALPHA, 12, 25 ),
	VII		( "NZJHGRCXMYSWBOUFAIVLPEKQDT", Keyboard.ALPHA, 12, 25 ),
	VIII	( "FKQHTLXOCBJSPDZRAMEWNIUYGV", Keyboard.ALPHA, 12, 25 ),

	// ------------------------------------------------------------------------
	// - M4 (U-boat) ----------------------------------------------------------
	// ------------------------------------------------------------------------

	BETA	( "LEYJVCNIXWPBQMDRTAKZGFUHOS", Keyboard.ALPHA ),
	GAMMA	( "FSOKANUERHMBTIYCWLQPZXVGJD", Keyboard.ALPHA ),
	;

	// ------------------------------------------------------------------------

	private final int [] notches;

	private final int [] forward;
	private final int [] backward;

	private int ringPosition;
	private int initialPosition;

	private int currentPosition;
	
	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	private Rotor( String data, Keyboard keyboard, int ... notches )
	{
		assert( data.length() == getPositionsCount() ) : "Invalid Rotor mapping data";

		this.notches = notches;

		this.forward = new int [ getPositionsCount() ];
		this.backward = new int [ getPositionsCount() ];
		for( int i = 0 ; i < getPositionsCount() ; i++ )
		{
			int z = charToPos( data.charAt( i ) );

			forward[ i ] = z;
			backward[ z ] = i;
		}

		setRingPosition( 0 );
		setInitialPosition( 'A' );
	}

	// ========================================================================
	// = Ring position ========================================================
	// ========================================================================

	/**
	 * Set the <i>Ringstellung</i> or ring position of the rotor. This changes
	 * the position of the hardware wiring relative to the start position.
	 * 
	 * @param ringPosition
	 */
	public void setRingPostition( char ringPosition )
	{
		check( ringPosition );

		this.ringPosition = charToPos( ringPosition );
	}

	/**
	 * Set the <i>Ringstellung</i> or ring position of the rotor. This changes
	 * the position of the hardware wiring relative to the start position.
	 * 
	 * @param ringPosition
	 */
	public void setRingPosition( int ringPosition )
	{
		check( ringPosition );

		this.ringPosition = ringPosition;
	}

	/**
	 * Get the <i>Ringstellung</i> or ring position of the rotor.
	 * 
	 * @return
	 */
	public int getRingPosition()
	{
		return ringPosition;
	}

	// ========================================================================
	// = Initial position =====================================================
	// ========================================================================

	/**
	 * 
	 * @param initialPosition
	 */
	public void setInitialPosition( char initialPosition )
	{
		check( initialPosition );

		this.initialPosition = charToPos( initialPosition );
	}

	/**
	 * 
	 * @param initialPosition
	 */
	public void setInitialPosition( int initialPosition )
	{
		check( initialPosition );

		this.initialPosition = initialPosition;
	}

	/**
	 * 
	 * @return
	 */
	public int getInitialPosition()
	{
		return initialPosition;
	}

	// ========================================================================
	// = Current position =====================================================
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public int getCurrentPosition()
	{
		return currentPosition;
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public boolean isAtNotch()
	{
		if( notches != null )
		{
			for( int i : notches )
			{
				if( currentPosition == i )
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 */
	public void turnOver()
	{
		 currentPosition = ++currentPosition % getPositionsCount();
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public void reset()
	{
		currentPosition = initialPosition;
	}

	// ========================================================================
	// = Substitution =========================================================
	// ========================================================================

	/**
	 * Forward path.
	 * 
	 * @param pos
	 * @return
	 */
	public int forward( int pos )
	{
		check( pos );

		return substitute( pos, forward );
	}

	/**
	 * Backward path.
	 * 
	 * @param pos
	 * @return
	 */
	public int backward( int pos )
	{
		check( pos );

		return substitute( pos, backward );
	}

	// ------------------------------------------------------------------------
	// - Internal -------------------------------------------------------------
	// ------------------------------------------------------------------------

	private int substitute( int pos, int [] mapping )
	{
		int shift = currentPosition - ringPosition;
		int target = (pos + shift + getPositionsCount()) % getPositionsCount();

		int tmp = mapping[ target ];
		tmp = (tmp - shift + getPositionsCount()) % getPositionsCount();

		return tmp;
	}
}
