package be.lmenten.enigma.hardware;

/**
 * 
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public enum Reflector
	implements Enigma
{
	B		( "YRUHQSLDPXNGOKMIEBFZCWVJAT" ),
	C		( "FVPJIAOYEDRZXWGCTKUQSBNMHL" ),

	B_THIN	( "ENKQAUYWJICOPBLMDXZVFTHRGS" ),
	C_THIN	( "RDOBJNTKVEHMLFCWZAXGYIPSUQ" ),
;

	private final int [] reflect;

	private Reflector( String data )
	{
		reflect = new int [ getPositionsCount() ];

		for( int i = 0 ; i < getPositionsCount() ; i++ )
		{
			int z = charToPos( data.charAt( i ) );

			reflect[z] = i;
		}
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public int reflect( int pos )
	{
		check( pos );

		return reflect[ pos ];
	}
}
