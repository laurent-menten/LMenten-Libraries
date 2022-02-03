package be.lmenten.utils.math.evaluator;

import java.util.function.DoubleBinaryOperator;

public non-sealed class Function2
	extends Function
{
	private final DoubleBinaryOperator operator;

	public Function2( String name, DoubleBinaryOperator operator )
	{
		super( name );

		this.operator = operator;
	}

	public double compute( double value1, double value2 )
	{
		return operator.applyAsDouble( value1, value2 );
	}
}
