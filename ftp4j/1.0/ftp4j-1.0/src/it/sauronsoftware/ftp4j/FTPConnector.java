package it.sauronsoftware.ftp4j;

import java.io.IOException;

/**
 * This inteface describes a connector. Connectors are used by the client to
 * establish connections with remote servers.
 * 
 * @author Carlo Pelliccia
 */
public interface FTPConnector {

	/**
	 * This methods returns an established connection to a remote host, suitable
	 * for a FTP communication channel.
	 * 
	 * @param host
	 *            The remote host name or address.
	 * @param port
	 *            The remote port.
	 * @return The connection with the remote host.
	 * @throws IOException
	 *             If the connection cannot be established.
	 */
	public FTPConnection connectForCommunicationChannel(String host, int port)
			throws IOException;

	/**
	 * This methods returns an established connection to a remote host, suitable
	 * for a FTP data transfer channel.
	 * 
	 * @param host
	 *            The remote host name or address.
	 * @param port
	 *            The remote port.
	 * @return The connection with the remote host.
	 * @throws IOException
	 *             If the connection cannot be established.
	 */
	public FTPConnection connectForDataTransferChannel(String host, int port)
			throws IOException;

}
