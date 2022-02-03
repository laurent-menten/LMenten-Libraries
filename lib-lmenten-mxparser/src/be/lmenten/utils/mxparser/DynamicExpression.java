/* ************************************************************************* *
 * Dynamic Expression for mXparser/java * Copyright (c) 2022+ Laurent MENTEN *
 * ------------------------------------------------------------------------- *
 * BSD 2-Clause "Simplified" Licence.                                        *
 *                                                                           *
 * You may use this software under the condition of Simplified BSD License.  *
 * Redistribution and use in source and binary forms, with or without        *
 * modification, are permitted provided that the following conditions are    *
 * met:                                                                      *
 *                                                                           *
 * 1. Redistributions of source code must retain the above copyright notice, *
 *    this list of conditions and the following disclaimer.                  *
 * 2. Redistributions in binary form must reproduce the above copyright      *
 *    notice, this list of conditions and the following disclaimer in the    *
 *    documentation and/or other materials provided with the distribution.   *
 *                                                                           *
 * THIS SOFTWARE IS PROVIDED BY LAURENT MENTEN "AS IS" AND ANY EXPRESS OR    *
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES *
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.   *
 * IN NO EVENT SHALL LAURENT MENTEN OR CONTRIBUTORS BE LIABLE FOR ANY        *
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL        *
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS   *
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)     *
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,       *
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  *
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE           *
 * POSSIBILITY OF SUCH DAMAGE.                                               *
 * ************************************************************************* */

package be.lmenten.utils.mxparser;

import be.lmenten.utils.mxparser.annotations.MXPF;
import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.parsertokens.ParserSymbol;
import org.mariuszgromada.math.mxparser.parsertokens.Token;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * This class adds dynamic call to objets methods for mXparser.
 * </p>
 *
 * <p>
 * Given an implementation of the marker interface FunctionExtensionDynamic that
 * provides methods returning a double and annotated with &#64;MXPF, and an instance
 * of the class NamedObject with a name and an arbitrary object reference. It is
 * possible to make a call to the method of the name given in an expression. The method is
 * selected according to its signature matching the type of the objects named in the expression.
 * </p>
 *<p>
 * See files in src-tests for a running example.
 *</p>
 * <p>
 * It works by patching the token list right after it was constructed and before it is
 * evaluated. The idea is to look for unmatched tokens that <i>looks like</i> functions,
 * and build a list of objects with the its parameters. Constants and Arguments values are
 * converted to Double and other tokens are looked up in the NamedObject list. After that
 * look for a method with a matching name in the FunctionExtensionDynamic list that also
 * has a signature matching the types in the array of parameters objects. We use the return
 * value of this method to replace the token and remove the parameters token.
 * </p>
 *
 * <p>
 * It just requires 3 tiny little modifications to the original mXparser Expression class.
 * <ol>
 * 		<li>Make <i>setExpressionModifiedFlag()</i> protected rather than package private.</li>
 *      <li>Add an empty <i>protected void postprocessInitialTokens( List&lt;Token&gt; initialTokens ) {}</i>
 *      	method.</li>
 *      <li>Make a call to <pre>postprocessInitialTokens( initialTokens );</pre> at the very end of
 *      	tokenizeExpressionString(), after calling evaluateTokensLevels()</li>
 * </ol>
 * </p>
 *
 * @version 1.0
 * @author Laurent Menten
 */
public class DynamicExpression
	extends Expression
{
	// ------------------------------------------------------------------------
	// - Borrowed from class mXparser -----------------------------------------
	// ------------------------------------------------------------------------

	private static final int NOT_FOUND = -1;
	private static final int FOUND = 0;

	private static final String FUNCTION	= "function";
	private static final String ARGUMENT	= "argument";
	private static final String UNITCONST	= "unit/const";
	private static final String ERROR		= "error";

	// ------------------------------------------------------------------------
	// - Own looksLike type ---------------------------------------------------
	// ------------------------------------------------------------------------

	private static final String PATCHED		= "patched";

	// ------------------------------------------------------------------------
	// - Functions and objects storage ----------------------------------------
	// ------------------------------------------------------------------------

	record DynamicFunctionInfo( String methodName, FunctionExtensionDynamic function, Method method )
	{
	}

	private final List<DynamicFunctionInfo> functionsInfoList
		= new ArrayList<>();

	private final List<NamedObject> objectsList
		= new ArrayList<>();

	// ------------------------------------------------------------------------

	private boolean repatchEveryCalculation = true;

	// ========================================================================
	// = Constructor(s) =======================================================
	// ========================================================================

	public DynamicExpression( PrimitiveElement ... elements )
	{
		super( elements );
	}

	public DynamicExpression( String expressionString, PrimitiveElement ... elements )
	{
		super( expressionString, elements );
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 *
	 * @return if true, reevaluate.
	 */
	public boolean getRepatchEveryCalculationMode()
	{
		return repatchEveryCalculation;
	}

	/**
	 *
	 * @param repatchEveryCalculation if true, reevaluate.
	 */
	public void setRepatchEveryCalculationMode( boolean repatchEveryCalculation )
	{
		this.repatchEveryCalculation = repatchEveryCalculation;
	}

	// ========================================================================
	// = Expression interface =================================================
	// ========================================================================

	@Override
	public double calculate()
	{
		double value = super.calculate();

		if( repatchEveryCalculation )
		{
			setExpressionModifiedFlag();
		}

		return value;
	}

	/**
	 * Do the magic.
	 *
	 * @param initialTokens the initial tokens list
	 */
	@Override
	protected void postprocessInitialTokens( List<Token> initialTokens )
	{
		if( getVerboseMode() )
		{
			mXparser.consolePrintTokens( initialTokens );
		}

		// --------------------------------------------------------------------
		// - Add looksLike to tokens list -------------------------------------
		// --------------------------------------------------------------------

		documentTokensList( initialTokens );

		// --------------------------------------------------------------------
		// - Loop through tokens list -----------------------------------------
		// --------------------------------------------------------------------

		int functionTokenIndex;
		for( int i = 0 ; i < initialTokens.size() ; i++ )
		{
			Token token = initialTokens.get( i );
			if( (token.tokenTypeId == Token.NOT_MATCHED) && FUNCTION.equals( token.looksLike ) )
			{
				functionTokenIndex = i;

				List<Token> discardedTokens
					= new ArrayList<>();

				// ------------------------------------------------------------
				// - Check if we know this function name ----------------------
				// ------------------------------------------------------------

				long count = functionsInfoList.stream()
					.filter( info -> info.methodName.equals( token.tokenStr ) )
					.count();

				if( count == 0 )
				{
					continue;
				}

				// ------------------------------------------------------------
				// - Check if we have at least 3 following tokens and if first
				// - is '('.
				// ------------------------------------------------------------

				if( (i + 3) >= initialTokens.size() )
				{
					continue;
				}

				if( initialTokens.get( i + 1 ).tokenId != ParserSymbol.LEFT_PARENTHESES_ID )
				{
					continue;
				}

				discardedTokens.add( initialTokens.get( i + 1 ) ); // Left parenthesis

				// ------------------------------------------------------------
				// - Make list of arguments -----------------------------------
				// ------------------------------------------------------------

				List<Object> args = new ArrayList<>();

				for( i = i + 2 ; i < initialTokens.size() && initialTokens.get( i ).tokenId != ParserSymbol.RIGHT_PARENTHESES_ID ; i++  )
				{
					discardedTokens.add( initialTokens.get( i ) );

					if( initialTokens.get( i ).tokenId == ParserSymbol.COMMA_ID )
					{
						continue;
					}

					Token argument = initialTokens.get( i );
					switch( argument.tokenTypeId )
					{
						case ParserSymbol.NUMBER_TYPE_ID:
						{
							args.add( argument.tokenValue );
							break;
						}

						case Constant.TYPE_ID:
						{
							Constant c = getConstant( argument.tokenStr );
							args.add( c.getConstantValue() );
							break;
						}

						case Argument.TYPE_ID:
						{
							Argument a = getArgument( argument.tokenStr );
							args.add( a.getArgumentValue() );
							break;
						}

						case Token.NOT_MATCHED:
						{
							if( ARGUMENT.equals( argument.looksLike ) )
							{
								List<NamedObject> candidates = objectsList.stream()
									.filter( o -> o.getName().equals( argument.tokenStr ) )
									.toList();

								if( candidates.size() == 1 )
								{
									args.add( candidates.get( 0 ).getObject() );
								}
							}

							break;
						}
					}
				}

				discardedTokens.add( initialTokens.get( i ) );	// right parenthesis

				// ------------------------------------------------------------
				// - Resolve value --------------------------------------------
				// ------------------------------------------------------------

				Double value = callDynamicFunctionMethod( token.tokenStr, args.toArray() );
				if( value != null )
				{
					Token func = initialTokens.get( functionTokenIndex );
					func.tokenStr = value.toString();
					func.keyWord = ParserSymbol.NUMBER_STR;
					func.tokenId = 1;
					func.tokenTypeId = ParserSymbol.NUMBER_TYPE_ID;
					func.tokenValue = value;
					func.looksLike = PATCHED;

					initialTokens.removeAll( discardedTokens );
					i -= discardedTokens.size();
				}
			}
		}

		if( getVerboseMode() )
		{
			mXparser.consolePrintTokens( initialTokens );
		}
	}

	/**
	 * Add the looksLike hit to tokens. This code is borrowed from original
	 * getCopyOfInitialTokens() method.
	 *
	 * @param tokensList the tokens list
	 */
	private void documentTokensList( List<Token> tokensList)
	{
		for( int i = 0; i < tokensList.size(); i++ )
		{
			Token token =  tokensList.get(i);
			if( token.tokenTypeId == Token.NOT_MATCHED )
			{
				if( mXparser.regexMatch(token.tokenStr, ParserSymbol.unitOnlyTokenRegExp ) )
				{
					token.looksLike = UNITCONST;
				}
				else if( mXparser.regexMatch(token.tokenStr, ParserSymbol.nameOnlyTokenRegExp ) )
				{
					token.looksLike = ARGUMENT;

					if( i < tokensList.size() - 1 )
					{
						Token tokenNext = tokensList.get( i + 1 );
						if( (tokenNext.tokenTypeId == ParserSymbol.TYPE_ID) && (tokenNext.tokenId == ParserSymbol.LEFT_PARENTHESES_ID) )
						{
							token.looksLike = FUNCTION;
						}
					}
				}
				else
				{
					token.looksLike = ERROR;
				}
			}
		}
	}

	/**
	 * Find and call a matching method.
	 *
	 * @param methodName the name of the method to call
	 * @param args the list of objects to use as parameters
	 * @return the return value or null
	 */
	private Double callDynamicFunctionMethod( String methodName, Object ... args )
	{
		// --------------------------------------------------------------------
		// - Filter out unwanted methods --------------------------------------
		// --------------------------------------------------------------------

		List<DynamicFunctionInfo> selectedList = functionsInfoList.stream()
				.filter( info -> info.methodName.equals( methodName ) )
				.toList();

		// --------------------------------------------------------------------
		// - Look for a method with a matching signature ----------------------
		// --------------------------------------------------------------------

		for( DynamicFunctionInfo info : selectedList )
		{
			Class<?> [] parameters = info.method.getParameterTypes();
			if( parameters.length != args.length )
			{
				continue;
			}

			boolean signatureOk = true;
			for( int i = 0 ; i < parameters.length ; i++ )
			{
				if( ! parameters[i].equals( args[i].getClass() ) )
				{
					signatureOk = false;
					break;
				}
			}

			// --------------------------------------------------------------------
			// - Found one... call it. --------------------------------------------
			// --------------------------------------------------------------------

			if( signatureOk )
			{
				try
				{
					return (Double) info.method.invoke( info.function, args );
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		}

		return null;
	}

	// ========================================================================
	// = Dynamic functions management =========================================
	// ========================================================================

	public void addDynamicFunctions( FunctionExtensionDynamic ... functions )
	{
		for( FunctionExtensionDynamic function : functions )
		{
			long count = functionsInfoList.stream()
				.filter( info -> (info.function == function) || info.function.getClass().equals( function.getClass() ) )
				.count();
			;

			if( count > 0 )
			{
				throw new IllegalArgumentException( "Instance or class already registered" );
			}

			// ----------------------------------------------------------------

			final List<Method> methods = new ArrayList<Method>();
			Class<?> klass = function.getClass();
			while( klass != Object.class )
			{
				for( final Method method : klass.getDeclaredMethods() )
				{
					if( method.isAnnotationPresent( MXPF.class ) )
					{
						if( ! method.getReturnType().equals( Double.TYPE ) )
						{
							throw new IllegalArgumentException( "Bad return type for method '" + method + "'" );
						}

						methods.add( method );
					}
				}

				klass = klass.getSuperclass();
			}

			// ----------------------------------------------------------------

			methods.forEach( method ->
			{
				DynamicFunctionInfo info = new DynamicFunctionInfo( method.getName(), function, method );
				functionsInfoList.add( info );
			} );
		}
	}

	public void removeDynamicFunctions( FunctionExtensionDynamic ... functions )
	{
		var removeList = Arrays.asList( functions );

		List<DynamicFunctionInfo> selectedList = functionsInfoList.stream()
			.filter( info -> removeList.contains( info.function ) )
			.toList();

		functionsInfoList.removeAll( selectedList );
	}

	public void removeAllDynamicFunctions()
	{
		functionsInfoList.clear();
	}

	// ========================================================================
	// = Named objects management =============================================
	// ========================================================================

	public void addNamedObjects( NamedObject ... objects )
	{
		for( NamedObject object : objects )
		{
			long count = objectsList.stream()
				.filter( o -> (o == object) || o.getName().equals( object.getName() ) )
				.count();

			if( count > 0 )
			{
				throw new IllegalArgumentException( "Instance or name already registered" );
			}

			objectsList.add( object );
		}
	}

	public void removeNamedObjects( NamedObject ... objects )
	{
		var removeList = Arrays.asList( objects );

		List<NamedObject> selectedList = objectsList.stream()
			.filter( o -> removeList.contains( o ) )
			.toList();

		objectsList.removeAll( selectedList );
	}

	public void removeAllNamedObjects()
	{
		objectsList.clear();
	}
}
