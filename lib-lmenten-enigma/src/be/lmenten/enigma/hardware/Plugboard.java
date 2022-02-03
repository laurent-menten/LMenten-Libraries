package be.lmenten.enigma.hardware;

/**
 * <i>Eintrittswalze</i>
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public class Plugboard
	implements Enigma
{
	private final int plugsCount;
	private int currentPlugsCount;

	private final int [] permutation;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Plugboard( int plugsCount )
	{
		this.plugsCount = plugsCount;

		currentPlugsCount = 0;

		// --------------------------------------------------------------------

		permutation = new int [ getPositionsCount() ];

		for( int i = 0 ; i < getPositionsCount() ; i++ )
		{
			permutation[i] = i;
		}
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param c1
	 * @param c2
	 */
	public void plug( char c1, char c2 )
	{
		check( c1 );

		int pos1 = charToPos( c1 );
		if( permutation[pos1] != pos1 )
		{
			throw new IllegalArgumentException( "'" + c1 + "' already plugged" );
		}

		// --------------------------------------------------------------------

		check( c2 );

		int pos2 = charToPos( c2 );
		if( permutation[pos2] != pos2 )
		{
			throw new IllegalArgumentException( "'" + c2 + "' already plugged" );
		}

		// --------------------------------------------------------------------

		if( ++currentPlugsCount > plugsCount )
		{
			throw new IllegalArgumentException( "Plugs count exceeded" );
		}

		// --------------------------------------------------------------------

		permutation[ pos1 ] = pos2;
		permutation[ pos2 ] = pos1;
	}

	/**
	 * 
	 * @param c
	 */
	public void unplug( char c )
	{
		check( c );

		int pos1 = charToPos( c );
		if( permutation[pos1] == pos1 )
		{
			throw new IllegalArgumentException( "'" + c + "' not plugged" );
		}

		int pos2 = permutation[ pos1 ];
		permutation[ pos1 ] = pos1;
		permutation[ pos2 ] = pos2;

		currentPlugsCount--;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean isPlugged( char c )
	{
		check( c );

		int pos = charToPos( c );

		return permutation[ pos ] != pos;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public int getPlugPair( char c )
	{
		check( c );

		return permutation[ charToPos( c ) ];
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public int forward( int pos )
	{
		return permutation[ pos ];
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public int backward( int pos )
	{
		return permutation[ pos ];
	}
}
