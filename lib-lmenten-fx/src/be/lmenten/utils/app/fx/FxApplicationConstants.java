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
}
