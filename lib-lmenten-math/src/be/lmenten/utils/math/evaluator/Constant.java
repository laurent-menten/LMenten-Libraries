package be.lmenten.utils.math.evaluator;

public class Constant
	implements ValueProvider
{
	private final String name;
	private final double value;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Constant( String name, double value )
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
}
