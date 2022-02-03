package be.lmenten.utils.app.fx;

import javafx.fxml.Initializable;

public interface FxController<T>
	extends Initializable
{
	public void setApplication( T application );
}
