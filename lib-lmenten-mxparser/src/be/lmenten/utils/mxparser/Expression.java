package be.lmenten.utils.mxparser;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.PrimitiveElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
	<pre>
		 Constant v = new Constant( "v", 1 );

		 MethodReference o1 = new MethodReference( "o1", this::getValue );
		 ObjectReference o2 = new ObjectReference<LocalDate>( "o2", LocalDate.now(), d -> (double) d.getDayOfMonth() );

		 Expression e1 = new Expression( "v + o1 + o2" );
		 e1.addConstants( v );
		 e1.addMethodReferences( o1, o2 );

		 double r = e1.calculate();
	</pre>
 *
	<pre>
		 Constant v = new Constant( "v", 1 );

		 Argument a1 = new Argument( "a1", new MethodReferenceArgument( this::getValue ) );
		 Argument a2 = new Argument( "a2", new ObjectReferenceArgument<LocalDate>( LocalDate.now(), d -> (double) d.getDayOfMonth() ) );

		 Expression e2 = new Expression( "v + a1 + a2" );
		 e2.addConstants( v );
		 e2.addArguments( a1, a2 );

		 double s = e2.calculate();
 	</pre>
 *
 */
public class Expression
	extends org.mariuszgromada.math.mxparser.Expression
{
	// Borrowed from mXparser.

	static final int NOT_FOUND = -1;
	static final int FOUND = 0;

	// ------------------------------------------------------------------------

	private final List<MethodReference> methodReferencesList
		= new ArrayList<>();

	// ========================================================================
	// =
	// ========================================================================

	public Expression( PrimitiveElement ... elements )
	{
		super( elements );

		init();
	}

	public Expression( String expressionString, PrimitiveElement ... elements )
	{
		super( expressionString, elements );

		init();
	}

	// ------------------------------------------------------------------------

	private Method setExpressionModifiedFlag = null;

	private void init()
	{
		Class<?> clazz = getClass();
		while( clazz != null && setExpressionModifiedFlag == null )
		{
			try
			{
				setExpressionModifiedFlag = clazz.getDeclaredMethod( "setExpressionModifiedFlag" );
			}
			catch( NoSuchMethodException ex )
			{
				clazz = clazz.getSuperclass();
			}
		}

		if( setExpressionModifiedFlag != null )
		{
			setExpressionModifiedFlag.setAccessible( true );
		}
		else
		{
			LOG.severe( "Could not change setExpressionModifiedFlag() accessibility." );
		}
	}

	private void priv_setExpressionModifiedFlag()
	{
		try
		{
			if( setExpressionModifiedFlag != null )
			{
				setExpressionModifiedFlag.invoke( this );
			}
		}
		catch( IllegalAccessException | InvocationTargetException ex )
		{
			LOG.log( Level.SEVERE, "setExpressionModifiedFlag()", ex );
		}
	}

	// ========================================================================
	// =
	// ========================================================================

	@Override
	public boolean checkSyntax()
	{
		if( ! super.checkSyntax() )
		{
			String [] mm = getMissingUserDefinedArguments();
			for( String m : mm )
			{
				MethodReference or = getMethodReference( m );
				if( or != null )
				{
					Argument a = or.getRelatedArgument();
					if( getArgument( a.getArgumentName() ) == null )
					{
						addArguments( a );
					}
				}
			}
		}

		return super.checkSyntax();
	}


	// ========================================================================
	// =
	// ========================================================================

	@Override
	public void addDefinitions( PrimitiveElement... elements )
	{
		super.addDefinitions( elements );

		for( PrimitiveElement e : elements )
		{
			if( e != null )
			{
				if( e.getMyTypeId() == MethodReference.TYPE_ID )
				{
					addMethodReferences( (MethodReference) e );
				}
			}
		}
	}

	@Override
	public void removeDefinitions( PrimitiveElement... elements )
	{
		super.removeDefinitions( elements );

		for( PrimitiveElement e : elements )
		{
			if( e != null )
			{
				if( e.getMyTypeId() == MethodReference.TYPE_ID )
				{
					removeMethodReferences( (MethodReference)e );
				}
			}
		}
	}

	// ========================================================================
	// =
	// ========================================================================

	public void addMethodReferences( MethodReference... references )
	{
		for( MethodReference reference : references )
		{
			if( reference != null )
			{
				methodReferencesList.add( reference );
//				reference.addRelatedExpression( this );
			}
		}

		priv_setExpressionModifiedFlag();
	}

	public void addMethodReferences( List<MethodReference> methodReferencesList )
	{
		this.methodReferencesList.addAll( methodReferencesList );

		for( MethodReference reference : methodReferencesList )
		{
//			reference.addRelatedExpression( this );
		}

		priv_setExpressionModifiedFlag();
	}

	public void defineMethodReference( String methodReferenceName, DoubleSupplier func )
	{
		MethodReference reference = new MethodReference( methodReferenceName, func );
//		reference.addRelatedExpression( this );

		methodReferencesList.add( reference );

		priv_setExpressionModifiedFlag();
	}

	public int getMethodReferenceIndex( String methodReferenceName )
	{
		int methodReferencesNumber = methodReferencesList.size();
		if( methodReferencesNumber > 0 )
		{
			int methodReferenceIndex = 0;
			int searchResult = NOT_FOUND;
			while( (methodReferenceIndex < methodReferencesNumber) && (searchResult == NOT_FOUND) )
			{
				if( methodReferencesList.get(methodReferenceIndex).getReferenceName().equals(methodReferenceName) )
					searchResult = FOUND;
				else
					methodReferenceIndex++;
			}
			if (searchResult == FOUND)
				return methodReferenceIndex;
			else
				return NOT_FOUND;
		} else
			return NOT_FOUND;
	}

	public MethodReference getMethodReference( String methodReferenceName )
	{
		int methodReferenceIndex = getMethodReferenceIndex( methodReferenceName );
		if (methodReferenceIndex == NOT_FOUND)
			return null;
		else
			return methodReferencesList.get( methodReferenceIndex );
	}

	public MethodReference getMethodReference( int methodReferenceIndex )
	{
		if( (methodReferenceIndex < 0) || (methodReferenceIndex >= methodReferencesList.size() ) )
			return null;
		else
			return methodReferencesList.get( methodReferenceIndex );
	}

	public int getMethodReferencesNumber()
	{
		return methodReferencesList.size();
	}

	public void removeMethodReferences( String... methodReferencesNames )
	{
		for( String methodReferenceName : methodReferencesNames )
		{
			int methodReferenceIndex = getConstantIndex( methodReferenceName );
			if( methodReferenceIndex != NOT_FOUND )
			{
				MethodReference reference = methodReferencesList.get( methodReferenceIndex );
//				reference.removeRelatedExpression( this );
				methodReferencesList.remove( methodReferenceIndex );
			}
		}

		priv_setExpressionModifiedFlag();
	}

	public void removeMethodReferences( MethodReference... methodReferences )
	{
		for( MethodReference reference : methodReferences )
		{
			if( reference != null )
			{
				methodReferencesList.remove( reference );
//				reference.removeRelatedExpression( this );
				priv_setExpressionModifiedFlag();
			}
		}
	}

	public void removeAllMethodReferences()
	{
		for( MethodReference reference : methodReferencesList )
		{
//			reference.removeRelatedExpression( this );
		}

		methodReferencesList.clear();

		priv_setExpressionModifiedFlag();
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( Expression.class.getName() );

}
