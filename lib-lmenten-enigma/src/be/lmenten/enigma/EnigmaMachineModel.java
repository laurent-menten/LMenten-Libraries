package be.lmenten.enigma;

import java.util.EnumSet;
import java.util.Set;

import be.lmenten.enigma.hardware.Reflector;
import be.lmenten.enigma.hardware.Rotor;

public enum EnigmaMachineModel
{
	// ------------------------------------------------------------------------
	// - 
	// ------------------------------------------------------------------------

	M3( "M3", "Kriegsmarine" )
	{
		public Set<Rotor> getRotorsSet()
		{
			return EnumSet.of(
					Rotor.I,
					Rotor.II,
					Rotor.III,
					Rotor.IV,
					Rotor.V,
					Rotor.VI,
					Rotor.VII, 
					Rotor.VIII
				);
		}

		public Set<Rotor> getRotorsSet2()
		{
			return EnumSet.noneOf( Rotor.class );
		}

		public Set<Reflector> getReflectorsSet()
		{
			return EnumSet.of(
					Reflector.B,
					Reflector.C
				);
		}
	},

	// ------------------------------------------------------------------------
	// - 
	// ------------------------------------------------------------------------

	M4( "M4", "Kriegsmarine - U-boot" )
	{
		public Set<Rotor> getRotorsSet()
		{
			return EnumSet.of(
					Rotor.I,
					Rotor.II,
					Rotor.III,
					Rotor.IV,
					Rotor.V,
					Rotor.VI,
					Rotor.VII, 
					Rotor.VIII
				);
		}

		public Set<Rotor> getRotorsSet2()
		{
			return EnumSet.of(
					Rotor.BETA, 
					Rotor.GAMMA
				);
		}

		public Set<Reflector> getReflectorsSet()
		{
			return EnumSet.of(
					Reflector.B_THIN,
					Reflector.C_THIN
				);
		}
	},

	;
	
	// ------------------------------------------------------------------------

	private final String name;
	private final String description;

	// ========================================================================
	// = 
	// ========================================================================

	private EnigmaMachineModel( String name, String description )
	{
		this.name = name;
		this.description = description;
	}

	// ------------------------------------------------------------------------

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	// ------------------------------------------------------------------------

	public abstract Set<Rotor> getRotorsSet();

	public abstract Set<Rotor> getRotorsSet2();

	public abstract Set<Reflector> getReflectorsSet();
}
