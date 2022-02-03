package be.lmenten.enigma.hardware;

/**
 * 
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public interface Enigma
{
	// ========================================================================
	// = 
	// ========================================================================

	public default int getPositionsCount()
	{
		return 26;
	}

	// ========================================================================
	// = Character <--> position convertion ===================================
	// ========================================================================

	/**
	 *
	 * @param c
	 * @return
	 */
	public default int charToPos( char c )
	{
		return c - 'A';
	}

	/**
	 *
	 * @param pos
	 * @return
	 */
	public default char posToChar( int pos )
	{
		return (char)('A' + pos);
	}

	// ========================================================================
	// = Validation ===========================================================
	// ========================================================================

	/**
	 * 
	 * @param c
	 */
	public default void check( char c )
	{
		if( (c < 'A') || (c > 'Z') )
		{
			throw new IllegalArgumentException( "Illegal character '" + c + "'" );
		}
	}

	/**
	 * 
	 * @param pos
	 */
	public default void check( int pos )
	{
		if( (pos < 0) || (pos > (getPositionsCount() - 1)) )
		{
			throw new IllegalArgumentException( "Illegal position " + pos );
		}
	}
}
