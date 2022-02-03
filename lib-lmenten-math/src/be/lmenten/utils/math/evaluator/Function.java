package be.lmenten.utils.math.evaluator;

public sealed abstract class Function
	implements Value
	permits Function0, Function1, Function2, Function3
{
	private final String name;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Function( String name )
	{
		this.name = name;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public String getName()
	{
		return name;
	}
}
