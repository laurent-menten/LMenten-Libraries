package be.lmenten.enigma.model;

import be.lmenten.enigma.EnigmaMachine;
import be.lmenten.enigma.hardware.EntryDisc;
import be.lmenten.enigma.hardware.Plugboard;
import be.lmenten.enigma.hardware.Reflector;
import be.lmenten.enigma.hardware.Rotor;

/**
 * 
 * 
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2021 / 09 / 14
 */
public class M4
	extends EnigmaMachine
{
	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public M4( Reflector ukw, Rotor r4, Rotor r3, Rotor r2, Rotor r1, EntryDisc etw )
	{
		super( ukw, etw );

		setRotor1( r1 );
		setRotor2( r2 );
		setRotor3( r3 );
		setRotor4( r4 );

		setPlugboard( new Plugboard( 10 ) );
	}

	@Override
	protected void setRotor4( Rotor rotor )
	{
		if( (rotor != Rotor.BETA) && (rotor != Rotor.GAMMA) )
		{
			throw new IllegalArgumentException( "4th rotor should be BETA or GAMMA" );
		}

		super.setRotor4( rotor );
	}

	@Override
	protected void setReflector( Reflector ukw )
	{
		if( (ukw != Reflector.B_THIN) && (ukw != Reflector.C_THIN) )
		{
			throw new IllegalArgumentException( "UKW should be B/thin or C/thin for M4 machine" );
		}

		super.setReflector( ukw );
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * Set initial rotary wheels positions
	 * 
	 * @param r4pos
	 * @param r3pos
	 * @param r2pos
	 * @param r1pos
	 */
	public void configure( char r4pos, char r3pos, char r2pos, char r1pos )
	{
		getRotor4().setInitialPosition( r4pos );
		getRotor3().setInitialPosition( r3pos );
		getRotor2().setInitialPosition( r2pos );
		getRotor1().setInitialPosition( r1pos );

		reset();
	}
}
