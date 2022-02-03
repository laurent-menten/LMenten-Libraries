package be.lmenten.enigma;

import be.lmenten.enigma.hardware.*;

/**
 * 
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public abstract class EnigmaMachine
	implements Enigma
{
	private EntryDisc etw;

	private Plugboard plugBoard;

	private Rotor r1;
	private Rotor r2;
	private Rotor r3;
	private Rotor r4;

	private Reflector ukw;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public EnigmaMachine( Reflector ukw, EntryDisc etw )
	{
		setEntryDisc( etw );
		setReflector( ukw );
	}

	// ========================================================================
	// = Setters ==============================================================
	// ========================================================================

	protected void setEntryDisc( EntryDisc etw )
	{
		this.etw = etw;
	}

	// ------------------------------------------------------------------------

	protected void setPlugboard( Plugboard plugboard)
	{
		this.plugBoard = plugboard;
	}

	// ------------------------------------------------------------------------

	protected void setRotor1( Rotor rotor )
	{
		this.r1 = rotor;
	}

	protected void setRotor2( Rotor rotor )
	{
		this.r2 = rotor;
	}

	protected void setRotor3( Rotor rotor )
	{
		this.r3 = rotor;
	}

	protected void setRotor4( Rotor rotor )
	{
		this.r4 = rotor;
	}

	// ------------------------------------------------------------------------

	protected void setReflector( Reflector ukw )
	{
		this.ukw = ukw;
	}

	// ========================================================================
	// = Getters ==============================================================
	// ========================================================================

	public EntryDisc getEntryDisc()
	{
		return etw;
	}

	// ------------------------------------------------------------------------

	public Plugboard getPlugBoard()
	{
		return plugBoard;
	}

	// ------------------------------------------------------------------------

	public Rotor getRotor1()
	{
		return r1;
	}

	public Rotor getRotor2()
	{
		return r1;
	}

	public Rotor getRotor3()
	{
		return r3;
	}

	public Rotor getRotor4()
	{
		return r4;
	}

	// ------------------------------------------------------------------------

	public Reflector getReflector()
	{
		return ukw;
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * Set plugs board permutations
	 * 
	 * @param c1
	 * @param c2
	 */
	public void plug( char c1, char c2 )
	{
		plugBoard.plug( c1, c2 );
	}

	/**
	 * 
	 * @param c
	 */
	public void unplug( char c )
	{
		plugBoard.unplug( c );
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 */
	public void reset()
	{
		r3.reset();
		r2.reset();
		r1.reset();
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 */
	private void rotate()
	{
		if( r2.isAtNotch() )
		{
			r2.turnOver();
            r3.turnOver();
        }

        else if( r1.isAtNotch() )
        {
            r2.turnOver();
        }

        r1.turnOver();
	}

	// ========================================================================
	// = 
	// ========================================================================

	/**
	 * 
	 * @param c
	 * @return
	 */
	public char input( char c )
	{
		return posToChar( input( charToPos( c ) ) );
	}

	/**
	 * 
	 * @param z
	 * @return
	 */
	public int input( int z )
	{
		rotate();

		z = etw.forward( z );

		z = plugBoard.forward( z );

		z = r1.forward( z );
		z = r2.forward( z );
		z = r3.forward( z );

		if( r4 != null )
		{
			z = r4.forward( z );
		}

		z = ukw.reflect( z );

		if( r4 != null )
		{
			z = r4.backward( z );
		}

		z = r3.backward( z );
		z = r2.backward( z );
		z = r1.backward( z );

		z = plugBoard.backward( z );

		return z;
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public String input( String in )
	{
		StringBuilder s = new StringBuilder();

		for( int i = 0 ; i < in.length() ; i++ )
		{
			s.append( input( in.charAt(i) ) );
		}

		return s.toString();
	}

	// ========================================================================
	// = 
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( (char)('A' + r3.getCurrentPosition()) )
		 .append( "-" )
		 .append( (char)('A' + r2.getCurrentPosition()) )
		 .append( "-" )
		 .append( (char)('A' + r1.getCurrentPosition()) )
		 ;

		return s.toString();
	}
}
