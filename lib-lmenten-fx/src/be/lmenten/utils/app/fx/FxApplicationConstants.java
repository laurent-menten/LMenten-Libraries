package be.lmenten.utils.app.fx;

import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:laurent.menten@gmail.com">Laurent Menten<a>
 * @version 1.0
 * @since 1.0 - 2022 / 01 / 02
 */
public interface FxApplicationConstants
{
    // ------------------------------------------------------------------------
    // - Formatters -----------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final DateTimeFormatter DATETIME_FORMAT
            = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    public static final DateTimeFormatter LOGFILE_DATETIME_FORMAT
            = DateTimeFormatter.ofPattern( "yyyyMMdd-HHmmss" );

    // ------------------------------------------------------------------------
    // - Properties -----------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final String PROPERTY_DEBUG
            = "be.lmenten.debug";

    public static final String PROPERTY_LOG_ANSI_OUTPUT
            = "be.lmenten.logAnsiOutput";

    public static final String PROPERTY_LOG_LEVEL
            = "be.lmenten.logLevel";

    public static final String PROPERTY_DISABLE_LOG_FILTER
            = "be.lmenten.logFilter.disabled";

    public static final String PROPERTY_LOG_FILTER
            = "be.lmenten.logFilter";

    public static final String PROPERTY_LOG_DISABLE_LOGFILE
            = "be.lmenten.disableLogfile";

    public static final String PROPERTY_LOG_KEEP_LOGFILE
            = "be.lmenten.keepLogfile";

    public static final String PROPERTY_DEBUG_SHOW_EXCEPTION_WINDOW
            = "be.lmenten.debug.showExceptionWindow";

    public static final String PROPERTY_DEBUG_SHOW_LOG_WINDOW
            = "be.lmenten.debug.showLogWindow";

    public static final String PROPERTY_LAUNCH4J_FLAG
            = "be.lmenten.launch4j";

    // ------------------------------------------------------------------------
    // - Preferences ----------------------------------------------------------
    // ------------------------------------------------------------------------

    public static final String PREFS_LOGGING_DIRECTORY
            = "be.menten.logging.directory";
}
