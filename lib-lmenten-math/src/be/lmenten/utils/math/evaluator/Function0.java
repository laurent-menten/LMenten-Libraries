package be.lmenten.utils.math.evaluator;

import java.util.function.DoubleSupplier;

public non-sealed class Function0
	extends Function
{
	private final DoubleSupplier supplier;

	public Function0( String name, DoubleSupplier supplier )
	{
		super( name );

		this.supplier = supplier;
	}

	public double compute()
	{
		return supplier.getAsDouble();
	}
}
