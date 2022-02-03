module lib.lmenten
{
    requires java.logging;
    requires java.sql;
	requires java.rmi;
	requires java.prefs;
	requires java.desktop;

	exports be.lmenten.utils.logging;
    exports be.lmenten.utils.lang;
    exports be.lmenten.utils.arch;
    exports be.lmenten.utils.app;
	exports be.lmenten.utils.jdbc;
	exports be.lmenten.utils.recent;
	exports be.lmenten.utils.io;
	exports be.lmenten.utils.net;
	exports be.lmenten.utils.settings;
	exports be.lmenten.utils.deepzoom;
	exports be.lmenten.utils.i18n;
	exports be.lmenten.utils;
}