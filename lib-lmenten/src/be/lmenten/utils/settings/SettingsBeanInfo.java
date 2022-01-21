package be.lmenten.utils.settings;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public abstract class SettingsBeanInfo
	extends SimpleBeanInfo
{
	public abstract PropertyDescriptor[] getPropertyDescriptors();

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

	protected abstract String $( String key );
}
