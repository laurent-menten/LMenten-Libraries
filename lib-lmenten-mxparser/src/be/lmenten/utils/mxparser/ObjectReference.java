package be.lmenten.utils.mxparser;

import java.util.function.ToDoubleFunction;

public class ObjectReference<T>
	extends MethodReference
{

	private T objectReference;
	private ToDoubleFunction<T> accessor;

	// ========================================================================
	// = Constructor(s) =======================================================
	// ========================================================================

	public ObjectReference( String referenceName, T objectReference, ToDoubleFunction<T> accessor )
	{
		this( referenceName, objectReference, accessor, "" );
	}

	public ObjectReference( String referenceName, T objectReference, ToDoubleFunction<T> accessor, String description )
	{
		super( referenceName, null, description );

		this.objectReference = objectReference;
		this.accessor = accessor;
	}

	// ========================================================================
	// = Constructor(s) =======================================================
	// ========================================================================

	public T getObjectReference()
	{
		return objectReference;
	}

	public void setObjectReference( T objectReference )
	{
		this.objectReference = objectReference;
		refreshValue();
	}

	// ------------------------------------------------------------------------

	public ToDoubleFunction<T> getAccessor()
	{
		return accessor;
	}

	public void setAccessor( ToDoubleFunction<T> accessor )
	{
		this.accessor = accessor;
		refreshValue();
	}

	// ------------------------------------------------------------------------

	@Override
	public void refreshValue()
	{
		relatedArgument.setArgumentValue( accessor.applyAsDouble( objectReference ) );
	}
}
