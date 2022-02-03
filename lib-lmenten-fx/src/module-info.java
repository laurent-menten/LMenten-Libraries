module lib.lmenten.fx
{
    exports be.lmenten.utils.app.fx;
	exports be.lmenten.utils.ui.fx;
	exports be.lmenten.utils.logging.fx;
	exports be.lmenten.utils.ui;

	requires lib.lmenten;

	requires org.jetbrains.annotations;

	requires java.logging;
	requires java.prefs;
	requires jdk.jsobject;
	requires java.desktop;

	requires javafx.graphics;
    requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
}