package it.sauronsoftware.ftp4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class implements a local server to make data transfer with the remote
 * FTP server.
 * 
 * @author Carlo Pelliccia
 */
class FTPDataTransferServer implements Runnable {

	/**
	 * The ServerSocket object wainting for the incoming connection.
	 */
	private ServerSocket serverSocket;

	/**
	 * The socket to be established with the remote host.
	 */
	private Socket socket;

	/**
	 * The exception thrown during the wait for a connection (if any!).
	 */
	private IOException exception;

	/**
	 * The thread executing the listening for incoming connection routine.
	 */
	private Thread thread;

	/**
	 * Build the object.
	 * 
	 * @throws FTPDataTransferException
	 *             If a I/O error occurs.
	 */
	public FTPDataTransferServer() throws FTPDataTransferException {
		// Create the server socket.
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(null);
		} catch (IOException e) {
			throw new FTPDataTransferException("Cannot open the ServerSocket",
					e);
		}
		// Start the thread.
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Returns the local port the server socket is bounded.
	 * 
	 * @return The local port.
	 */
	public int getPort() {
		return serverSocket.getLocalPort();
	}

	public void run() {
		try {
			// Set the socket timeout.
			serverSocket.setSoTimeout(30000);
			// Wait for the incoming connection.
			socket = serverSocket.accept();
		} catch (IOException e) {
			exception = e;
		} finally {
			// Close the server socket.
			try {
				serverSocket.close();
			} catch (IOException e) {
				;
			}
		}
	}

	/**
	 * Close the server and interrupts every operating stream.
	 */
	public void close() {
		// Close the server socket (if open).
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				;
			}
		}
		// Close the socket (if open).
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				;
			}
		}
	}

	/**
	 * Returns the FTPConnection to exchange data with the remote side.
	 * 
	 * @return The FTPConnection.
	 * @throws FTPDataTransferException
	 *             If a I/O error occurs.
	 */
	public FTPConnection getConnection() throws FTPDataTransferException {
		if (socket == null && exception == null) {
			try {
				thread.join();
			} catch (Exception e) {
				;
			}
		}
		if (exception != null) {
			throw new FTPDataTransferException(
					"Cannot receive the incoming connection", exception);
		}
		if (socket == null) {
			throw new FTPDataTransferException("No socket available");
		}
		return new ServerConnection(socket);
	}

	private static class ServerConnection implements FTPConnection {

		private Socket socket;

		public ServerConnection(Socket socket) {
			this.socket = socket;
		}

		public void close() throws IOException {
			socket.close();
		}

		public InputStream getInputStream() throws IOException {
			return socket.getInputStream();
		}

		public OutputStream getOutputStream() throws IOException {
			return socket.getOutputStream();
		}

	}

}
