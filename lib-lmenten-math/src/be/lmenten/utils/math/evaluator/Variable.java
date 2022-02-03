package be.lmenten.utils.math.evaluator;

public class Variable
	implements ValueConsumer, ValueProvider
{
	private final String name;
	private double value;

	// ========================================================================
	// = Constructors =========================================================
	// ========================================================================

	public Variable( String name )
	{
		this( name, Double.NaN );
	}

	public Variable( String  name, double value )
	{
		this.name = name;
		this.value = value;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public String getName()
	{
		return name;
	}

	// ========================================================================
	// = ValueProvider interface ==============================================
	// ========================================================================

	@Override
	public double getValue()
	{
		return value;
	}

	// ========================================================================
	// = ValueConsumer interface ==============================================
	// ========================================================================

	@Override
	public void setValue( double value )
	{
		this.value = value;
	}
}
