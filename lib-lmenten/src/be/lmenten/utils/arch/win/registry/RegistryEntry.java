package be.lmenten.utils.arch.win.registry;

// FIXME add type parameter for values

public class RegistryEntry
{
	private final String key;
	private final String valueName;
	private final RegistryEntryType type;

	private String value;

	// ========================================================================
	// = 
	// ========================================================================

	public RegistryEntry( String key, String valueName, RegistryEntryType type )
	{
		this( key, valueName, type, null );
	}

	public RegistryEntry( String key, String valueName, RegistryEntryType type, String value )
	{
		this.key = key;
		this.valueName = valueName;
		this.type = type;
		this.value = value;
	}

	// ========================================================================
	// = 
	// ========================================================================

	public String getKey()
	{
		return key;
	}

	public String getValueName() {
		return valueName;
	}

	public RegistryEntryType getType()
	{
		return type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue( String value )
	{
		this.value = value;
	}
}
