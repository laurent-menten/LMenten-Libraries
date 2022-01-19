package be.lmenten.utils.app.fx;

import java.beans.*;

import org.jetbrains.annotations.PropertyKey;

public class FxApplicationSettingsBeanInfo
		extends SimpleBeanInfo
{
	@Override
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			var p1 = new PropertyDescriptor( "debugMode", FxApplicationSettings.class );
			var p2 = new PropertyDescriptor( "logAnsiOutput", FxApplicationSettings.class );
			var p3 = new PropertyDescriptor( "logLevel", FxApplicationSettings.class );
			var p4 = new PropertyDescriptor( "disableLogFilter", FxApplicationSettings.class );
			var p5 = new PropertyDescriptor( "logFilter", FxApplicationSettings.class );
			var p6 = new PropertyDescriptor( "disableLogfile", FxApplicationSettings.class );
			var p7 = new PropertyDescriptor( "keepLogfile", FxApplicationSettings.class );
			var p8 = new PropertyDescriptor( "showLogWindow", FxApplicationSettings.class );
			var p9 = new PropertyDescriptor( "showExceptionWindow", FxApplicationSettings.class );

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

	private String $( @PropertyKey( resourceBundle=FxApplication.RES_FQN ) String key )
	{
		return FxApplication.RES.getString( key );
	}
}
