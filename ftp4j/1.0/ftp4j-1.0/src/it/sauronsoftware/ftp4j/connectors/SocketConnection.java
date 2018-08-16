package it.sauronsoftware.ftp4j.connectors;

import it.sauronsoftware.ftp4j.FTPConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The SocketConnection class wraps together a socket, an input and an output
 * streams.
 * 
 * @author Carlo Pelliccia
 */
class SocketConnection implements FTPConnection {

	private Socket socket = null;

	private InputStream in = null;

	private OutputStream out = null;

	public SocketConnection(Socket socket, InputStream in, OutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	public SocketConnection(Socket socket) throws IOException {
		this.socket = socket;
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
	}

	public void close() throws IOException {
		socket.close();
	}

	public InputStream getInputStream() {
		return in;
	}

	public OutputStream getOutputStream() {
		return out;
	}

}
