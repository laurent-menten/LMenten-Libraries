package be.lmenten.utils.mxparser;

import org.mariuszgromada.math.mxparser.ArgumentExtension;

import java.util.function.DoubleSupplier;

public class MethodReferenceArgument
	implements ArgumentExtension
{
	private final DoubleSupplier method;

	private double currentValue;

	public MethodReferenceArgument( DoubleSupplier method )
	{
		this.method = method;

		refreshValue();
	}

	public void refreshValue()
	{
		currentValue = method.getAsDouble();
	}

	@Override
	public double getArgumentValue()
	{
		return currentValue;
	}

	@Override
	public ArgumentExtension clone()
	{
		return new MethodReferenceArgument( method );
	}
}
