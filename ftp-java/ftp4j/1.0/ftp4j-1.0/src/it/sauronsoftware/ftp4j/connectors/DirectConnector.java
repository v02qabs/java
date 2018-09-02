package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnection;
import it.sauronsoftware.ftp4j.FTPConnector;

import java.io.IOException;
import java.net.Socket;

/**
 * The DirectConnector connects the remote host with a straight socket
 * connection, using no proxy.
 * 
 * @author Carlo Pelliccia
 */
public class DirectConnector implements FTPConnector {

	private FTPConnection connect(String host, int port) throws IOException {
		Socket socket = new Socket(host, port);
		return new SocketConnection(socket);
	}

	public FTPConnection connectForCommunicationChannel(String host, int port)
			throws IOException {
		return connect(host, port);
	}

	public FTPConnection connectForDataTransferChannel(String host, int port)
			throws IOException {
		return connect(host, port);
	}

}
