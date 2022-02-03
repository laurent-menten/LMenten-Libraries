package be.lmenten.utils.math.evaluator;

import java.util.function.DoubleUnaryOperator;

public non-sealed class Function1
	extends Function
{
	private final DoubleUnaryOperator operator;

	public Function1( String name, DoubleUnaryOperator operator )
	{
		super( name );

		this.operator = operator;
	}

	public double compute( double value )
	{
		return operator.applyAsDouble( value );
	}
}
