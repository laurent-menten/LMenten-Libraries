package be.lmenten.utils.mxparser;

import org.mariuszgromada.math.mxparser.ArgumentExtension;

import java.util.function.ToDoubleFunction;

public class ObjectReferenceArgument<T>
	implements ArgumentExtension
{
	private final T objectReference;
	private final ToDoubleFunction<T> function;

	private double currentValue;

	public ObjectReferenceArgument( T objectReference, ToDoubleFunction<T> function )
	{
		this.objectReference = objectReference;
		this.function = function;

		refreshValue();
	}

	public void refreshValue()
	{
		currentValue = function.applyAsDouble( objectReference );
	}

	@Override
	public double getArgumentValue()
	{
		return currentValue;
	}

	@Override
	public ArgumentExtension clone()
	{
		return new ObjectReferenceArgument<T>( objectReference, function );
	}
}
