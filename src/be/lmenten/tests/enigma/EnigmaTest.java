package be.lmenten.tests.enigma;

import be.lmenten.enigma.model.M4;
import be.lmenten.enigma.EnigmaMachine;
import be.lmenten.enigma.hardware.EntryDisc;
import be.lmenten.enigma.hardware.Reflector;
import be.lmenten.enigma.hardware.Rotor;

public class EnigmaTest
{
	public static void main( String[] args )
	{
		String s = "Laurent Menten";
		String s_in = s.replace( " ", "" ).toUpperCase();

		System.out.println( "original: '" + s + "'" );
		System.out.println( "      in: '" + s_in + "'" );

		EnigmaMachine e1 = new M4( Reflector.B_THIN, Rotor.BETA, Rotor.I, Rotor.II, Rotor.III, EntryDisc.ALPHA );
		e1.getPlugBoard().plug( 'L', 'B' );
		e1.reset();
		String s_cyphered =  e1.input( s_in );
		System.out.println( "cyphered: '" + s_cyphered + "'" );

		EnigmaMachine e2 = new M4( Reflector.B_THIN, Rotor.BETA, Rotor.I, Rotor.II, Rotor.III, EntryDisc.ALPHA );
		e2.getPlugBoard().plug( 'L', 'B' );
		e2.reset();
		String s_out =  e1.input( s_cyphered );
		System.out.println( "     out: '" + s_out + "'" );

		System.out.println( "  passed: " + s_out.equals( s_in ) );
	}
}
