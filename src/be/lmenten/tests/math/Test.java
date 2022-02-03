package be.lmenten.tests.math;

import be.lmenten.utils.math.evaluator.*;
import be.lmenten.utils.math.evaluator.grammar.lexer.LexerException;
import be.lmenten.utils.math.evaluator.grammar.parser.ParserException;
import be.lmenten.utils.math.evaluator.utils.ParseUtils;

public class Test
{
	public static void main( String[] args )
	{
		String [] expressions =
		{
			"ans",								// NaN : (initial value)
			"true",								// 1.0 : constant
			"ans",								// 1.0 : always last result, if error in expression no change
		"#",
			"0/0",								// NaN
			"1/0",								// Infinity
			"-1/0",								// -Infinity
		"#",
			"1#1#2",							// 1 1/2 : Fraction
			"centimeters( 1#3#8 )",				// 1 3/8 inches -> cm
			"inches( 1 )",						// 1 cm -> inches
		"#",
			"test()",							// same name 0 parameters instance of Function0
			"test( 1 )",						//   "    "  1 parameter instance of Function1
			"test( 1, 2 )",						//   "    "  2 parameters instance of Function2
			"test( 1, 2, 3 )",					//   "    "  2 parameters instance of Function3
		"#",
			"cos( x )",							// ERROR: x not known, add variable x in catch
			"cos( 1 ) -> x",					// no more exception
		"#",
			"sin( cos( tan ( pi ) ) )",			// nesting
		"#",
			"log( e )",							//
			"log10( 10 )",						//
			"min( random(), random() )",		//
		"#",
			"-1",								//
			"- 1",								// ERROR: no space allowed
			"x",								//
			"-x",								//
			"- x",								// ERROR: no space allowed
		"#",
			"1 -> x",							// assignation to variable
			"1 -> pi",							// ERROR: assignation to constant
			"x",								//
			"sin( 1 ) -> x",					// assignation to variable
			"x",								//
		"#",
			"myLine.length",					// call getter for property of object
			"myLine.angle",						// call getter for property of object
			"1 -> myLine.length",				// call setter for property of object
			"myLine.length",					// call getter for property of object
		"#",
			"if( true, 10, 20 )",				//
			"if( false, 10, 20 )",				//
			"if( 1 = 0, 0, 10 )",				//
			"if( 1 = 1, 0, 10 )",				//
			"if( 1 < 0, 0, 10 )",				//
			"if( 1 <= 2, 0, 10 )",				//
			"if( 1 <> 0, 0, 10 )",				//
			"if( 1 <> 1, 0, 10 )",				//
			"if( 1 > 0, 0, 10 )",				//
			"if( 1 >= 2, 0, 10 )",				//
		"#",
			"if( true & false, 0, 10 )",		//
			"if( true | false, 0, 10 )",		//
		"#",
			"if( true ^ true, 0, 10 )",			//
			"if( true ^ false, 0, 10 )",		//
			"if( false ^ true, 0, 10 )",		//
			"if( false ^ false, 0, 10 )",		//
		"#",
			"test( true, @1 ) -> x",			// lexer error
			"test( true, 1 0 ) -> x",			// parser error
		"#",
			"true -> acc.reset",				// accumulator object
			"acc.sum",							// = 0
			"1 -> acc.accumulate",				// += 1
			"2 -> acc.accumulate",				// += 2
			"3 -> acc.accumulate",				// += 3
			"acc.sum",							// = 6 as NamedObject property
			"acc",								// = 6 as ValueProvider

			"length( myline )"
		};

		// --------------------------------------------------------------------

		Constant cc = new Constant( "c", 1.0 );

		ExpressionEvaluator i = new ExpressionEvaluator();
		i.addConstants( cc );

		Function f0 = new Function0( "test", () -> 1 );
		Function f1 = new Function1( "test", (a) -> 2 * a );
		Function f2 = new Function2( "test", ( a, b) -> a + b );
		Function f3 = new Function3( "test", (a,b,c) -> a + b * c );
		i.addFunctions( f0, f1, f2, f3 );

		NamedObject no = new NamedObject( "myLine", new Line( 110, 45) );
		i.addNamedObjects( no );

		// --------------------------------------------------------------------

		for( String expression : expressions )
		{
			if( expression.trim().startsWith( "#" ) )
			{
				System.out.println();
				continue;
			}

			try
			{
				System.out.println( expression + " = " + i.evaluate( expression ) );
			}
			catch( Throwable ex )
			{
				System.out.println( expression + " *** ERROR *** " + ex.getMessage() );

				if( i.getVariable( "x" ) == null )
				{
					Variable x = new Variable( "x" );
					i.addVariables( x );
				}

				if( ex instanceof ParserException pex )
				{
					System.out.println( ParseUtils.indicator( pex.getToken().getPos() ) );
				}
				else if( ex instanceof LexerException lex )
				{
					System.out.println( ParseUtils.indicator( lex.getToken().getPos() ) );
				}
			}
		}
	}
}
