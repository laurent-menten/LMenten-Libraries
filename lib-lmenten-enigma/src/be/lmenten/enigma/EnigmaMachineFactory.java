package be.lmenten.enigma;

import be.lmenten.enigma.hardware.EntryDisc;
import be.lmenten.enigma.hardware.Reflector;
import be.lmenten.enigma.hardware.Rotor;
import be.lmenten.enigma.model.M3;
import be.lmenten.enigma.model.M4;

public class EnigmaMachineFactory
{
	public static M3 getM3Instance( Reflector ukw, Rotor r3, Rotor r2, Rotor r1, EntryDisc etw )
	{
		return new M3( ukw, r3, r2, r1, etw );
	}

	public static M4 getM4Instance( Reflector ukw, Rotor r4, Rotor r3, Rotor r2, Rotor r1, EntryDisc etw )
	{
		return new M4( ukw, r4, r3, r2, r1, etw );
	}
}
