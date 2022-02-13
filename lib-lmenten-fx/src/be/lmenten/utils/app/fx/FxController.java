package be.lmenten.utils.app.fx;

import javafx.fxml.Initializable;

public abstract class FxController<T extends FxApplication>
	implements Initializable
{
	private T application;

	public void setApplication( T application )
	{
		this.application = application;
	}

	public T getApplication()
	{
		return application;
	}
}
