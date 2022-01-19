package be.lmenten.utils.ui.fx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public final class I18N
{
	private static final List<Locale> supportedLocales;
	private static final ObjectProperty<Locale> currentLocale;

	static
	{
		supportedLocales = new ArrayList<>();
		supportedLocales.add( Locale.ENGLISH );

		currentLocale = new SimpleObjectProperty<>( getDefaultLocale() );
		currentLocale.addListener( (object, oldValue, newValue) ->
		{
			Locale.setDefault( newValue );	
		} );
	}

	// ------------------------------------------------------------------------

	private final Class<?> clazz;
	private ResourceBundle rb;

	// ========================================================================
	// =
	// ========================================================================

	public I18N( Class<?> clazz, Locale ... supportedLocales )
	{
		this.clazz = clazz;

		for( Locale supportedLocale : supportedLocales )
		{
			I18N.supportedLocales.add( supportedLocale );
		}

		rb = ResourceBundle.getBundle( clazz.getName() );
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public Locale getLocale()
	{
		return currentLocale.get();
	}

	/**
	 * 
	 * @param locale
	 */
	public void setLocale( Locale locale )
	{
        rb = ResourceBundle.getBundle( clazz.getName(), locale );

        localeProperty().set( locale );
		
        Locale.setDefault( locale );
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default locale. This is the systems default if contained in
	 * the supported locales, english otherwise.
	 * 
	 * @return
	 */
	public static Locale getDefaultLocale()
	{
		Locale sysDefault = Locale.getDefault();

		return supportedLocales.contains( sysDefault ) ? sysDefault : Locale.ENGLISH;
	}

	/**
	 * Get the supported Locales.
	 * 
	 * @return list of Locale objects.
	 */
	public List<Locale> getSupportedLocales()
	{
		return supportedLocales;
	}

	public void addSupportedLocale( Locale locale )
	{
		supportedLocales.add( locale );
	}

	// ------------------------------------------------------------------------

	public static ObjectProperty<Locale> localeProperty()
	{
        return currentLocale;
    }

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * Gets the string with the given key from the resource bundle for the
	 * current locale and uses it as first argument to MessageFormat.format,
	 * passing in the optional args and returning the result.
	 * 
	 * @param key message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public String get( final String key, final Object... args )
	{
		return MessageFormat.format( rb.getString( key ), args );
	}

	/**
	 * Creates a String binding to a localized String for the given message
	 * bundle key.
	 * 
	 * @param key message key
	 * @param args optional arguments for the message
	 * @return String binding
	 */
	public StringBinding createStringBinding( final String key, Object... args )
	{
		return Bindings.createStringBinding( () -> get(key, args), currentLocale );
	}

	/**
	 * Creates a String Binding to a localized String that is computed by
	 * calling the given func.
	 * 
	 * @param func function called on every change
	 * @return String binding
	 */
	public StringBinding createStringBinding( Callable<String> func )
	{
		return Bindings.createStringBinding( func, currentLocale );
	}

	// ========================================================================
	// =
	// ========================================================================

	/**
	 * Creates a bound Label whose value is computed on language change.
	 * 
	 * @param func the function to compute the value
	 * @return Label
	 */
	public Label labelForValue( Callable<String> func )
	{
		Label label = new Label();
		label.textProperty().bind( createStringBinding(func) );
        return label;
    }

	/**
	 * Creates a bound Button for the given ResourceBundle key.
	 * 
	 * @param key ResourceBundle key
	 * @param args optional arguments for the message
	 * @return Button
	 */
	public Button buttonForKey( final String key, final Object... args )
	{
		Button button = new Button();
		button.textProperty().bind( createStringBinding( key, args ) );
        return button;
    }
}