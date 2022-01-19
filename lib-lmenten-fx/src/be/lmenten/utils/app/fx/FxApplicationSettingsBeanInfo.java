package be.lmenten.utils.app.fx;

import java.beans.*;

import org.jetbrains.annotations.PropertyKey;

public class FxApplicationSettingsBeanInfo
		extends SimpleBeanInfo
{
	// Stolen from org.controlfx.property.editor.BeanProperty to remove
	// dependency.

	protected static final String CATEGORY_LABEL_KEY = "propertysheet.item.category.label";

	// ========================================================================
	// =
	// ========================================================================

	@Override
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			var p1 = new PropertyDescriptor( "debugModeEnabled", FxApplicationSettings.class );
			p1.setDisplayName( $("app.fx.setting.debugModeEnabled") );
			p1.setShortDescription( $("app.fx.setting.debugModeEnabled.description") );

			// ----------------------------------------------------------------

			var p2 = new PropertyDescriptor( "logLevel", FxApplicationSettings.class );
			p2.setDisplayName( $("app.fx.setting.logLevel") );
			p2.setShortDescription( $("app.fx.setting.logLevel.description") );
			p2.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.logger") );

			var p3 = new PropertyDescriptor( "logFilter", FxApplicationSettings.class );
			p3.setDisplayName( $("app.fx.setting.logFilter") );
			p3.setShortDescription( $("app.fx.setting.logFilter.description") );
			p3.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.logger") );

			// ----------------------------------------------------------------

			var p4 = new PropertyDescriptor( "logDir", FxApplicationSettings.class );
			p4.setDisplayName( $("app.fx.setting.logDir") );
			p4.setShortDescription( $("app.fx.setting.logDir.description") );
			p4.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.logfile") );

			var p5 = new PropertyDescriptor( "logFileEnabled", FxApplicationSettings.class );
			p5.setDisplayName( $("app.fx.setting.logFileEnabled") );
			p5.setShortDescription( $("app.fx.setting.logFileEnabled.description") );
			p5.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.logfile") );

			var p6 = new PropertyDescriptor( "keepLogFileEnabled", FxApplicationSettings.class );
			p6.setDisplayName( $("app.fx.setting.keepLogFileEnabled") );
			p6.setShortDescription( $("app.fx.setting.keepLogFileEnabled.description") );
			p6.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.logfile") );

			// ----------------------------------------------------------------

			var p7 = new PropertyDescriptor( "ansiLogOutputEnabled", FxApplicationSettings.class );
			p7.setDisplayName( $("app.fx.setting.ansiLogOutputEnabled") );
			p7.setShortDescription( $("app.fx.setting.ansiLogOutputEnabled.description") );
			p7.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.ui") );

			var p8 = new PropertyDescriptor( "showLogWindowEnabled", FxApplicationSettings.class );
			p8.setDisplayName( $("app.fx.setting.showLogWindowEnabled") );
			p8.setShortDescription( $("app.fx.setting.showLogWindowEnabled.description") );
			p8.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.ui") );

			var p9 = new PropertyDescriptor( "showExceptionWindowEnabled", FxApplicationSettings.class );
			p9.setDisplayName( $("app.fx.setting.showExceptionWindowEnabled") );
			p9.setShortDescription( $("app.fx.setting.showExceptionWindowEnabled.description") );
			p9.setValue( CATEGORY_LABEL_KEY, $("app.fx.setting.category.ui") );

			return new PropertyDescriptor [] { p1, p2, p3, p4, p5, p6, p7, p8, p9 };
		}
		catch( IntrospectionException e )
		{
			e.printStackTrace();
		}

		return new PropertyDescriptor [0];
	}

	@Override
	public MethodDescriptor[] getMethodDescriptors()
	{
		return new MethodDescriptor [0];
	}

	@Override
	public EventSetDescriptor[] getEventSetDescriptors()
	{
		return new EventSetDescriptor [0];
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private String $( @PropertyKey( resourceBundle=FxApplication.RESOURCE_FQN) String key )
	{
		return FxApplication.RESOURCE.getString( key );
	}
}
