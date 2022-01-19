package be.lmenten.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils
{
	public static byte [] retrieveFile( String localUrl, String remoteUrl )
	{
		byte [] md5 = null;

		LOG.fine( "Downloading file '" + remoteUrl + "' to '" + localUrl + "'." );

		try
		{
			File localFile = new File( localUrl );
			URL url = new URL( remoteUrl );

			MessageDigest md = MessageDigest.getInstance( "MD5" );

			try( InputStream is = url.openStream();
					DigestInputStream dis = new DigestInputStream( is, md );
					ReadableByteChannel readableByteChannel = Channels.newChannel( dis );
					FileOutputStream fileOutputStream = new FileOutputStream( localFile ) )
			{
				fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

				md5 = md.digest();
			}
		}
		catch( NoSuchAlgorithmException ex )
		{
			LOG.log( Level.SEVERE, "MD5 algorithm", ex );
		}
		catch( IOException ex )
		{
			LOG.log( Level.WARNING, "Download error", ex );
		}

		return md5;
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final Logger LOG
		= Logger.getLogger( FileUtils.class.getName() );
}
