package be.lmenten.utils.mxparser;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.PrimitiveElement;
import org.mariuszgromada.math.mxparser.mXparser;
import org.mariuszgromada.math.mxparser.parsertokens.ParserSymbol;

import java.util.function.DoubleSupplier;

public class MethodReference
	extends PrimitiveElement
{
	public static final int TYPE_ID = 200;
	public static final String TYPE_DESC = "User defined method reference";

	// ------------------------------------------------------------------------

	public static final boolean NO_SYNTAX_ERRORS = Expression.NO_SYNTAX_ERRORS;
	public static final boolean SYNTAX_ERROR_OR_STATUS_UNKNOWN = Expression.SYNTAX_ERROR_OR_STATUS_UNKNOWN;
	private static final String NO_SYNTAX_ERROR_MSG = "Method reference - no syntax errors.";

	// ------------------------------------------------------------------------

	private String referenceName;
	private DoubleSupplier methodReference;
	private String description;

	private Argument relatedArgument;

	// ------------------------------------------------------------------------

	private boolean syntaxStatus;
	private String errorMessage;

	// ========================================================================
	// = Constructor(s) =======================================================
	// ========================================================================

	public MethodReference( String referenceName, DoubleSupplier method )
	{
		this( referenceName, method, "" );
	}

	public MethodReference( String referenceName, DoubleSupplier method, String referenceDescription )
	{
		super( MethodReference.TYPE_ID );

		if ( mXparser.regexMatch( referenceName, ParserSymbol.nameOnlyTokenOptBracketsRegExp ) )
		{
			this.referenceName = referenceName;
			this.methodReference = method;
			this.description = referenceDescription;

			this.relatedArgument = new Argument( referenceName );
			refreshValue();

			syntaxStatus = NO_SYNTAX_ERRORS;
			errorMessage = NO_SYNTAX_ERROR_MSG;
		}
		else
		{
			syntaxStatus = SYNTAX_ERROR_OR_STATUS_UNKNOWN;
			errorMessage = "[" + referenceName + "] " + "--> invalid method reference name, pattern not matches: " + ParserSymbol.nameOnlyTokenOptBracketsRegExp;
		}
	}

	// ========================================================================
	// = Getters/Setters ======================================================
	// ========================================================================

	public String getReferenceName()
	{
		return referenceName;
	}

	public void setReferenceName( String referenceName )
	{
		if( mXparser.regexMatch( referenceName, ParserSymbol.nameOnlyTokenOptBracketsRegExp ) )
		{
			this.referenceName = referenceName;
			relatedArgument.setArgumentName( referenceName );
		}
		else
		{
			syntaxStatus = SYNTAX_ERROR_OR_STATUS_UNKNOWN;
			errorMessage = "[" + referenceName + "] " + "--> invalid method reference name, pattern not matches: " + ParserSymbol.nameOnlyTokenOptBracketsRegExp;
		}
	}

	// ------------------------------------------------------------------------

	public Argument getRelatedArgument()
	{
		return relatedArgument;
	}

	public void refreshValue()
	{
		relatedArgument.setArgumentValue( methodReference.getAsDouble() );
	}

	public double getValue()
	{
		return relatedArgument.getArgumentValue();
	}

	// ------------------------------------------------------------------------

	public DoubleSupplier getMethodReference()
	{
		return methodReference;
	}

	public void setMethodReference( DoubleSupplier methodReference )
	{
		this.methodReference = methodReference;
		refreshValue();
	}

	// ------------------------------------------------------------------------

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	// ------------------------------------------------------------------------

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public boolean getSyntaxStatus()
	{
		return syntaxStatus;
	}
}
