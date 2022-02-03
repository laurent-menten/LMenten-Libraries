package be.lmenten.utils.math.evaluator;

public non-sealed class Function3
	extends Function
{
	private final DoubleTrinaryOperator operator;

	public Function3( String name, DoubleTrinaryOperator operator )
	{
		super( name );

		this.operator = operator;
	}

	public double compute( double value1, double value2, double value3 )
	{
		return operator.applyAsDouble( value1, value2, value3 );
	}
}
