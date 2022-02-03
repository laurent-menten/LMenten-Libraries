package be.lmenten.utils.math.evaluator;

public class Accumulator
	implements ValueProvider
{
	private double value;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Accumulator()
	{
		value = 0.0D;
	}

	public void setReset( double value )
	{
		if( value == ExpressionEvaluator.DOUBLE_TRUE )
		{
			this.value = 0.0D;
		}
	}

	public void setAccumulate( double value )
	{
		this.value += value;
	}

	public double getSum()
	{
		return value;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public String getName()
	{
		return null;
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
