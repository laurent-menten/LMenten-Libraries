package be.lmenten.utils.math.evaluator;

import be.lmenten.utils.math.evaluator.grammar.analysis.DepthFirstAdapter;
import be.lmenten.utils.math.evaluator.grammar.lexer.Lexer;
import be.lmenten.utils.math.evaluator.grammar.lexer.LexerException;
import be.lmenten.utils.math.evaluator.grammar.parser.Parser;
import be.lmenten.utils.math.evaluator.grammar.parser.ParserException;
import be.lmenten.utils.math.evaluator.utils.ParseUtils;
import be.lmenten.utils.math.evaluator.grammar.node.*;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 */
public class ExpressionEvaluator
	extends DepthFirstAdapter
{
	public static final double DOUBLE_FALSE = 0.0D;
	public static final double DOUBLE_TRUE = 1.0D;

	// ------------------------------------------------------------------------
	// - Builtin --------------------------------------------------------------
	// ------------------------------------------------------------------------

	private static final Constant [] builtinConstants =
	{
		new Constant( "false", DOUBLE_FALSE ),
		new Constant( "true", DOUBLE_TRUE ),
		new Constant( "NaN", Double.NaN ),
		new Constant( "Infinity", Double.POSITIVE_INFINITY ),
		new Constant( "pi", Math.PI ),
		new Constant( "e", Math.E ),
	};

	// ------------------------------------------------------------------------

	private final Variable ans = new Variable( "ans" );

	private final Variable [] builtinVariables =
	{
		ans,
		new Variable( "tmp" )
	};

	// ------------------------------------------------------------------------

	private final NamedObject [] builtinNamedObjects =
	{
		new NamedObject( "acc", new Accumulator() ),
	};

	// ------------------------------------------------------------------------

	private static final Function [] builtinFunctions =
	{
		new Function0( "random", Math::random ),

		new Function1( "abs", Math::abs ),
		new Function1( "acos", Math::acos ),
		new Function1( "asin", Math::asin ),
		new Function1( "atan", Math::atan ),
		new Function1( "cbrt", Math::cbrt ),
		new Function1( "ceil", Math::ceil ),
		new Function1( "centimeters", v -> v * 2.54D ),
		new Function1( "cos", Math::cos ),
		new Function1( "cosh", Math::cosh ),
		new Function1( "degrees", Math::toDegrees ),
		new Function1( "exp", Math::exp ),
		new Function1( "expm1", Math::expm1 ),
		new Function1( "floor", Math::floor ),
		new Function1( "inches", v -> v / 2.54D ),
		new Function1( "log", Math::log ),
		new Function1( "log10", Math::log10 ),
		new Function1( "log1p", Math::log1p ),
		new Function1( "radians", Math::toRadians ),
		new Function1( "signum", Math::signum ),
		new Function1( "sin", Math::sin ),
		new Function1( "sinh", Math::sinh ),
		new Function1( "sqrt", Math::sqrt ),
		new Function1( "tan", Math::tan ),
		new Function1( "tanh", Math::tanh ),

		new Function2( "atan2", Math::atan2 ),
		new Function2( "hypot", Math::hypot ),
		new Function2( "max", Math::max ),
		new Function2( "min", Math::min ),
		new Function2( "pow", Math::pow ),

		new Function3( "if", (v1, v2, v3) -> ( v1 == DOUBLE_TRUE ) ? v2 : v3 ),
	};

	// ------------------------------------------------------------------------
	// - Internal state -------------------------------------------------------
	// ------------------------------------------------------------------------

	private final Map<String,ValueProvider> valueProviders
		= new HashMap<>();

	// ------------------------------------------------------------------------

	private final Map<String,Function0> functions0
		= new HashMap<>();

	private final Map<String,Function1> functions1
		= new HashMap<>();

	private final Map<String,Function2> functions2
		= new HashMap<>();

	private final Map<String,Function3> functions3
		= new HashMap<>();

	// ------------------------------------------------------------------------

	private final Map<String,NamedObject> namedObjects
		= new HashMap<>();

	// ------------------------------------------------------------------------

	Stack<Double> stack = new Stack<>();

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	/**
	 *
	 */
	public ExpressionEvaluator()
	{
		for( ValueProvider valueProvider : builtinConstants )
		{
			addValueProvider( valueProvider );
		}

		for( ValueProvider valueProvider : builtinVariables )
		{
			addValueProvider( valueProvider );
		}

		for( Function function : builtinFunctions )
		{
			addFunctions( function );
		}

		for( NamedObject namedObject : builtinNamedObjects )
		{
			addNamedObjects( namedObject );
		}
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 *
	 * @param expression
	 * @return
	 */
	public double evaluate( String expression )
		throws  LexerException, ParserException
	{
		stack.clear();

		try( StringReader sr = new StringReader( expression ) )
		{
			try( PushbackReader pr = new PushbackReader( sr ) )
			{
				Lexer lexer = new Lexer( pr );

				Parser parser = new Parser( lexer );
				Start ast = parser.parse();

				ast.apply( this );

				return ans.getValue();
			}
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}

		return Double.NaN;
	}

	// ========================================================================
	// = ValueProvider management =============================================
	// ========================================================================

	/**
	 *
	 * @param constants list of constants
	 */
	public void addConstants( Constant ... constants )
	{
		addValueProviders( constants );
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public Constant getConstant( String name )
	{
		var valueProvider = valueProviders.get( name );
		if( valueProvider instanceof Constant constant )
		{
			return constant;
		}

		return null;
	}

	/**
	 *
	 * @param variables list of variables
	 */
	public void addVariables( Variable ... variables )
	{
		addValueProviders( variables );
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public Variable getVariable( String name )
	{
		var valueProvider = valueProviders.get( name );
		if( valueProvider instanceof Variable variable )
		{
			return variable;
		}

		return null;
	}

	// ------------------------------------------------------------------------

	/**
	 *
	 * @param namedObjects list of named objects
	 */
	public void addNamedObjects( NamedObject ... namedObjects )
	{
		for( NamedObject namedObject : namedObjects )
		{
			this.namedObjects.put( namedObject.getName(), namedObject );
		}
	}

	public NamedObject getNamedObject( String name )
	{
		return namedObjects.get( name );
	}

	// ------------------------------------------------------------------------

	/**
	 *
	 * @param functions list of functions
	 */
	public void addFunctions( Function ... functions )
	{
		for( Function function : functions )
		{
			if( function instanceof Function0 f )
			{
				this.functions0.put( function.getName(), f );
			}
			else if( function instanceof Function1 f )
			{
				this.functions1.put( function.getName(), f );
			}
			else if( function instanceof Function2 f )
			{
				this.functions2.put( function.getName(), f );
			}
			else if( function instanceof Function3 f )
			{
				this.functions3.put( function.getName(), f );
			}
		}
	}

	/**
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	public Function getFunction( String name, Class<? extends Function> clazz )
	{
		if( clazz == Function0.class )
		{
			return functions0.get( name );
		}
		else if( clazz == Function1.class )
		{
			return functions1.get( name );
		}
		else if( clazz == Function2.class )
		{
			return functions2.get( name );
		}
		else if( clazz == Function3.class )
		{
			return functions3.get( name );
		}

		return null;
	}

	// ------------------------------------------------------------------------

	/**
	 *
	 * @param valueProviders list of value providers
	 */
	protected void addValueProviders( ValueProvider ... valueProviders )
	{
		for( ValueProvider valueProvider : valueProviders )
		{
			addValueProvider( valueProvider );
		}
	}

	/**
	 *
	 * @param valueProvider a value provider
	 */
	protected void addValueProvider( ValueProvider valueProvider )
	{
		if( ! this.valueProviders.containsKey( valueProvider.getName()) )
		{
			this.valueProviders.put( valueProvider.getName(), valueProvider );
		}
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	protected ValueProvider getValueProvider( String name )
	{
		return valueProviders.get( name );
	}

	// ========================================================================
	// = Finish state =========================================================
	// ========================================================================

	@Override
	public void outStart( Start node )
	{
		if( stack.size() != 1 )
		{
			throw new IllegalStateException( "Illegal stack state" );
		}

		ans.setValue( stack.pop() );
	}
/*
	@Override
	public void inAVarExp( AVarExp node )
	{
		String varName = node.getIdentifier().getText();
		System.out.println( "> " + varName);
	}

	@Override
	public void outAVarExp( AVarExp node )
	{
		String varName = node.getIdentifier().getText();
		System.out.println( "< " + varName);
	}

	@Override
	public void outAConstExp( AConstExp node )
	{
		String constName = node.getIdentifier().getText();
		System.out.println(constName);
	}

	@Override
	public void inADeleteExp( ADeleteExp node )
	{
		String varName = node.getIdentifier().getText();
		System.out.println(varName);

		stack.push( 0. );
	}
*/
	// ========================================================================
	// = Assignation ==========================================================
	// ========================================================================

	@Override
	public void outAAssignVarExp( AAssignVarExp node )
	{
		String varName = node.getIdentifier().getText().trim();
		double n = stack.peek();

		ValueProvider v = valueProviders.get( varName );
		if( v == null )
		{
			throw new IllegalArgumentException( "Unknown variable " + varName );
		}

		if( v instanceof Variable variable )
		{
			variable.setValue( n );
		}
		else
		{
			throw new IllegalArgumentException( varName + " is not a variable" );
		}
	}

	@Override
	public void outAAssignObjExp( AAssignObjExp node )
	{
		String reference = node.getObjIdentifier().getText();
		String [] splitReference = reference.split( "\\." );

		String objectName = splitReference[0];
		String propertyName = splitReference[1];
		String propertyGetterName = "set" + propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );

		NamedObject namedObject = namedObjects.get( objectName );
		if( namedObject == null )
		{
			throw new IllegalArgumentException( "Unknown object: " + objectName );
		}

		Object target = namedObject.getObject();

		try
		{
			Method method = target.getClass().getMethod( propertyGetterName, Double.TYPE );

			method.invoke( target, stack.peek() );
		}
		catch( NoSuchMethodException ex )
		{
			throw new IllegalArgumentException( "Setter for property '"  +
					target.getClass().getSimpleName() + "." + propertyName + "' not found", ex );
		}
		catch( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex )
		{
			throw new IllegalArgumentException( "Invocation of " +
					target.getClass().getSimpleName() + "." + propertyGetterName + "() failed", ex );
		}
	}

	// ========================================================================
	// = Associative operators ================================================
	// ========================================================================

	@Override
	public void outAMulExp( AMulExp node )
	{
		stack.push( stack.pop() * stack.pop() );
	}

	@Override
	public void outAAddExp( AAddExp node )
	{
		stack.push( stack.pop() + stack.pop() );
	}

	// ========================================================================
	// = Non-associative operators ============================================
	// ========================================================================

	@Override
	public void outADivExp( ADivExp node )
	{
		Double n = stack.pop();

		stack.push( stack.pop() / n );
	}

	@Override
	public void outAModExp( AModExp node )
	{
		Double n = stack.pop();

		stack.push( stack.pop() % n );
	}

	@Override
	public void outASubExp( ASubExp node )
	{
		double n = stack.pop();

		stack.push( stack.pop() - n );
	}

	// ========================================================================
	// = Logical operators ====================================================
	// ========================================================================

	@Override
	public void outAEqualExp( AEqualExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 == n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outANotEqualExp( ANotEqualExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 != n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outALThanExp( ALThanExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 < n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outALEqualExp( ALEqualExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 <= n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outAGThanExp( AGThanExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 > n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outAGEqualExp( AGEqualExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( (n2 >= n1) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outAAndExp( AAndExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( ((n2 == DOUBLE_TRUE) && (n1 == DOUBLE_TRUE)) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outAOrExp( AOrExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( ((n2 == DOUBLE_TRUE) || (n1 == DOUBLE_TRUE)) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	@Override
	public void outAXorExp( AXorExp node )
	{
		double n1 = stack.pop();
		double n2 = stack.pop();

		stack.push( ((n2 == DOUBLE_TRUE) ^ (n1 == DOUBLE_TRUE)) ? DOUBLE_TRUE : DOUBLE_FALSE );
	}

	// ========================================================================
	// = Unary ================================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	// - Integers -------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAInteger2Exp( AInteger2Exp node )
	{
		String number = node.getInteger2().getText();

		double n = (double) ParseUtils.parseBinary( number );

		stack.push( n );
	}

	@Override
	public void inAInteger8Exp( AInteger8Exp node )
	{
		String number = node.getInteger8().getText();

		double n = (double) ParseUtils.parseOctal( number );

		stack.push( n );
	}

	@Override
	public void inAInteger10Exp( AInteger10Exp node )
	{
		String number = node.getInteger10().getText();

		double n = (double) ParseUtils.parseDecimal( number );

		stack.push( n );
	}

	@Override
	public void inAInteger16Exp( AInteger16Exp node )
	{
		String number = node.getInteger16().getText();

		double n = (double) ParseUtils.parseHexadecimal( number );

		stack.push( n );
	}

	// ------------------------------------------------------------------------
	// - Float and fractions --------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAFloatExp( AFloatExp node )
	{
		String number = node.getFloat().getText();

		double n = ParseUtils.parseFloatingPoint( number );

		stack.push( n );
	}

	@Override
	public void inAFractionExp( AFractionExp node )
	{
		String number = node.getFraction().getText();

		double n = ParseUtils.parseFraction( number );

		stack.push( n );
	}

	// ------------------------------------------------------------------------
	// - Functions ------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void outAFunc0Exp( AFunc0Exp node )
	{
		String funcName = node.getIdentifier().getText();

		Function0 function = functions0.get( funcName );
		if( function != null )
		{
			stack.push(  function.compute() );
		}
		else
		{
			throw new UnsupportedOperationException( "Unknown function " + funcName + "()" );
		}
	}

	@Override
	public void outAFunc1Exp( AFunc1Exp node )
	{
		String funcName = node.getIdentifier().getText();
		Double n = stack.pop();

		Function1 function = functions1.get( funcName );
		if( function != null )
		{
			stack.push(  function.compute( n ) );
		}
		else
		{
			throw new UnsupportedOperationException( "Unknown function " + funcName + "(a)" );
		}
	}

	@Override
	public void outAFunc2Exp( AFunc2Exp node )
	{
		String funcName = node.getIdentifier().getText();
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		Function2 function = functions2.get( funcName );
		if( function != null )
		{
			stack.push(  function.compute( n1, n2 ) );
		}
		else
		{
			throw new UnsupportedOperationException( "Unknown Function " + funcName + "(a,b)" );
		}
	}

	@Override
	public void outAFunc3Exp( AFunc3Exp node )
	{
		String funcName = node.getIdentifier().getText();
		Double n1 = stack.pop();
		Double n2 = stack.pop();
		Double n3 = stack.pop();

		Function3 function = functions3.get( funcName );
		if( function != null )
		{
			stack.push(  function.compute( n3, n2, n1 ) );
		}
		else
		{
			throw new UnsupportedOperationException( "Unknown function " + funcName + "(a,b,c)" );
		}
	}

	// ------------------------------------------------------------------------
	// - Variable and getters -------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAGetterExp( AGetterExp node )
	{
		String reference = node.getObjIdentifier().getText();
		double sign = 1.0D;

		if( reference.startsWith( "-" ) )
		{
			reference = reference.substring( 1 );
			sign = -1.0D;
		}

		String [] splitReference = reference.split( "\\." );

		String objectName = splitReference[0];
		String propertyName = splitReference[1];
		String propertyGetterName = "get" + propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );

		NamedObject namedObject = namedObjects.get( objectName );
		if( namedObject == null )
		{
			throw new IllegalArgumentException( "Unknown object: " + objectName );
		}

		Object target = namedObject.getObject();

		try
		{
			Method method = target.getClass().getMethod( propertyGetterName );

			double n = (double) method.invoke( target );

			stack.push( sign * n );
		}
		catch( NoSuchMethodException ex )
		{
			throw new IllegalArgumentException( "Getter for property '"  +
				target.getClass().getSimpleName() + "." + propertyName + "' not found", ex );
		}
		catch( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex )
		{
			throw new IllegalArgumentException( "Invocation of " +
				target.getClass().getSimpleName() + "." + propertyGetterName + "() failed", ex );
		}
	}

	@Override
	public void inAValueExp( AValueExp node )
	{
		String varName = node.getIdentifier().getText().trim();
		double sign = 1.0D;

		if( varName.startsWith( "-" ) )
		{
			varName = varName.substring( 1 );
			sign = -1.0D;
		}

		ValueProvider v = valueProviders.get( varName );
		if( v != null )
		{
			stack.push( sign * v.getValue() );
		}
		else
		{
			NamedObject namedObject = namedObjects.get( varName );
			if( namedObject != null && namedObject.getObject() instanceof ValueProvider v2 )
			{
				stack.push( sign * v2.getValue() );
			}
			else
			{
				throw new IllegalArgumentException( "Unknown constant/variable: " + varName );
			}
		}
	}
}
