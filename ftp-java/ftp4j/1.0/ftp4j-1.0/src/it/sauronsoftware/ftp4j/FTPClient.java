package it.sauronsoftware.ftp4j;

import it.sauronsoftware.ftp4j.connectors.DirectConnector;
import it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer;
import it.sauronsoftware.ftp4j.extrecognizers.ParametricTextualExtensionRecognizer;
import it.sauronsoftware.ftp4j.listparsers.DOSListParser;
import it.sauronsoftware.ftp4j.listparsers.EPLFListParser;
import it.sauronsoftware.ftp4j.listparsers.NetWareListParser;
import it.sauronsoftware.ftp4j.listparsers.UnixListParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements a FTP client.
 * 
 * You can use an instance of this class to connect to a remote FTP site and do
 * FTP operations like directory listing, file upload and download, resume a
 * broken upload/download and so on.
 * 
 * The common flow is: create the object, connect to a remote FTP site with the
 * connect() method, authenticate with login(), do anything you need with the
 * contents of the remote site, quit the site with disconnect().
 * 
 * A FTPClient object can handle a connection per time. Once you have used and
 * disconnected a FTPClient object you can use it again to connect another FTP
 * server.
 * 
 * @author Carlo Pelliccia
 */
public class FTPClient {

	/**
	 * The constant for the AUTO file transfer type. It lets the client pick
	 * between textual and binary types, depending on the extension of the file
	 * exchanged throgh a textual extension recognizer.
	 */
	public static final int TYPE_AUTO = 0;

	/**
	 * The constant for the TEXTUAL file transfer type. It means that the data
	 * sent or received is treated as textual information. This implies charset
	 * conversion during the transfer.
	 */
	public static final int TYPE_TEXTUAL = 1;

	/**
	 * The constant for the BINARY file transfer type. It means that the data
	 * sent or received is treated as a binary stream. The data is taken "as
	 * is", without any charset conversion.
	 */
	public static final int TYPE_BINARY = 2;

	/**
	 * The DateFormat object used to parse the reply to a MDTM command.
	 */
	private static final DateFormat MDTM_DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * The RegExp Pattern object used to parse the reply to a PASV command.
	 */
	private static final Pattern PASV_PATTERN = Pattern
			.compile("\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}");

	/**
	 * The RegExp Pattern object used to parse the reply to a PWD command.
	 */
	private static final Pattern PWD_PATTERN = Pattern.compile("\"/.*\"");

	/**
	 * The connector used to connect the remote host.
	 */
	private FTPConnector connector = new DirectConnector();

	/**
	 * The FTPCommunicationListener objects registered on the client.
	 */
	private ArrayList communicationListeners = new ArrayList();

	/**
	 * The FTPListParser objects registrered on the client.
	 */
	private ArrayList listParsers = new ArrayList();

	/**
	 * The textual extension recognizer used by the client.
	 */
	private FTPTextualExtensionRecognizer textualExtensionRecognizer = DefaultTextualExtensionRecognizer
			.getInstance();

	/**
	 * The FTPListParser used successfully during previous connection-scope list
	 * operations.
	 */
	private FTPListParser parser = null;

	/**
	 * If the client is connected, it reports the remote host name or address.
	 */
	private String host = null;

	/**
	 * If the client is connected, it reports the remote port number.
	 */
	private int port = 0;

	/**
	 * If the client is authenticated, it reports the authentication username.
	 */
	private String username;

	/**
	 * If the client is authenticated, it reports the authentication password.
	 */
	private String password;

	/**
	 * The flag reporting the connection status.
	 */
	private boolean connected = false;

	/**
	 * The flag reporting the authentication status.
	 */
	private boolean authenticated = false;

	/**
	 * The flag for the passive FTP data transfer mode. Default value is true,
	 * cause it's usually the preferred FTP operating mode.
	 */
	private boolean passive = true;

	/**
	 * The type of the data transfer contents (auto, textual, binary). The value
	 * should be one of TYPE_AUTO, TYPE_TEXTUAL and TYPE_BINARY constants.
	 * Default value is TYPE_AUTO.
	 */
	private int type = TYPE_AUTO;

	/**
	 * This flag reports if there's any ongoing abortable data transfer
	 * operation. Its value should be accessed only under the eye of the
	 * abortLock synchronization object.
	 */
	private boolean ongoingDataTransfer = false;

	/**
	 * The InputStream used for data transfert operations.
	 */
	private InputStream dataTransferInputStream = null;

	/**
	 * The OutputStream used for data transfert operations.
	 */
	private OutputStream dataTransferOutputStream = null;

	/**
	 * This flag turns to true when any data transfer stream is closed due to an
	 * abort suggestion.
	 */
	private boolean aborted = false;

	/**
	 * Lock object used for synchronization.
	 */
	private Object lock = new Object();

	/**
	 * Lock object used for synchronization in abort operations.
	 */
	private Object abortLock = new Object();

	/**
	 * The communication channel established with the server.
	 */
	private FTPCommunicationChannel communication = null;

	/**
	 * Builds and initializes the client.
	 */
	public FTPClient() {
		// The built-in parsers.
		addListParser(new UnixListParser());
		addListParser(new DOSListParser());
		addListParser(new EPLFListParser());
		addListParser(new NetWareListParser());
	}

	/**
	 * This method returns the connector used to connect the remote host.
	 * 
	 * @return The connector used to connect the remote host.
	 */
	public FTPConnector getConnector() {
		return connector;
	}

	/**
	 * This method sets the connector used to connect the remote host.
	 * 
	 * Default one is a
	 * it.sauronsoftware.ftp4j.connectors.direct.DirectConnector instance.
	 * 
	 * @param connector
	 *            The connector used to connect the remote host.
	 * @see DirectConnector
	 */
	public void setConnector(FTPConnector connector) {
		this.connector = connector;
	}

	/**
	 * This method enables/disables the use of the passive mode.
	 * 
	 * @param passive
	 *            If true the passive mode is enabled.
	 */
	public void setPassive(boolean passive) {
		synchronized (lock) {
			this.passive = passive;
		}
	}

	/**
	 * This methods sets how to treat the contents during a file tranfer.
	 * 
	 * The type supplied should be one of TYPE_AUTO, TYPE_TEXTUAL or TYPE_BINARY
	 * constants. Default value is TYPE_AUTO.
	 * 
	 * TYPE_TEXTUAL means that the data sent or received is treated as textual
	 * information. This implies charset conversion during the transfer.
	 * 
	 * TYPE_BINARY means that the data sent or received is treated as a binary
	 * stream. The data is taken "as is", without any charset conversion.
	 * 
	 * TYPE_AUTO lets the client pick between textual and binary types,
	 * depending on the extension of the file exchanged, using a
	 * FTPTextualExtensionRecognizer instance, which could be setted through the
	 * setTextualExtensionRecognizer method. The default recognizer is an
	 * instance of
	 * it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer.
	 * 
	 * @param type
	 *            The type.
	 * @throws IllegalArgumentException
	 *             If the supplied type is not valid.
	 * @see FTPClient#setTextualExtensionRecognizer(FTPTextualExtensionRecognizer)
	 * @see DefaultTextualExtensionRecognizer
	 */
	public void setType(int type) throws IllegalArgumentException {
		if (type != TYPE_AUTO && type != TYPE_BINARY && type != TYPE_TEXTUAL) {
			throw new IllegalArgumentException("Invalid type");
		}
		synchronized (lock) {
			this.type = type;
		}
	}

	/**
	 * This method returns the value suggesting how the client encode and decode
	 * the contents during a data transfer.
	 * 
	 * @return The type as a numeric value. The value could be compared to the
	 *         constants TYPE_AUTO, TYPE_BINARY and TYPE_TEXTUAL.
	 */
	public int getType() {
		synchronized (lock) {
			return type;
		}
	}

	/**
	 * This method returns the textual extension recognizer used by the client.
	 * 
	 * Default one is
	 * it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer.
	 * 
	 * @return The textual extension recognizer used by the client.
	 * @see DefaultTextualExtensionRecognizer
	 */
	public FTPTextualExtensionRecognizer getTextualExtensionRecognizer() {
		synchronized (lock) {
			return textualExtensionRecognizer;
		}
	}

	/**
	 * This method sets the textual extension recognizer used by the client.
	 * 
	 * The default one is
	 * it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer.
	 * 
	 * You can plug tour own implementing the FTPTextualExtensionRecognizer
	 * interface. For your convenience the ftp4j gives you another
	 * FTPTextualExtensionRecognizer implementation, which is
	 * it.sauronsoftware.ftp4j.extrecognizers.ParametricTextualExtensionRecognizer.
	 * 
	 * @param textualExtensionRecognizer
	 *            The textual extension recognizer used by the client.
	 * @see DefaultTextualExtensionRecognizer
	 * @see ParametricTextualExtensionRecognizer
	 */
	public void setTextualExtensionRecognizer(
			FTPTextualExtensionRecognizer textualExtensionRecognizer) {
		synchronized (lock) {
			this.textualExtensionRecognizer = textualExtensionRecognizer;
		}
	}

	/**
	 * This method tests if this client is authenticated.
	 * 
	 * @return true if this client is authenticated, false otherwise.
	 */
	public boolean isAuthenticated() {
		synchronized (lock) {
			return authenticated;
		}
	}

	/**
	 * This method tests if this client is connected to a remote FTP server.
	 * 
	 * @return true if this client is connected to a remote FTP server, false
	 *         otherwise.
	 */
	public boolean isConnected() {
		synchronized (lock) {
			return connected;
		}
	}

	/**
	 * This method tests if this client works in passive FTP mode.
	 * 
	 * @return true if this client is configured to work in passive FTP mode.
	 */
	public boolean isPassive() {
		synchronized (lock) {
			return passive;
		}
	}

	/**
	 * If the client is connected, it reports the remote host name or address.
	 * 
	 * @return The remote host name or address.
	 */
	public String getHost() {
		synchronized (lock) {
			return host;
		}
	}

	/**
	 * If the client is connected, it reports the remote port number.
	 * 
	 * @return The remote port number.
	 */
	public int getPort() {
		synchronized (lock) {
			return port;
		}
	}

	/**
	 * If the client is authenticated, it reports the authentication password.
	 * 
	 * @return The authentication password.
	 */
	public String getPassword() {
		synchronized (lock) {
			return password;
		}
	}

	/**
	 * If the client is authenticated, it reports the authentication username.
	 * 
	 * @return The authentication username.
	 */
	public String getUsername() {
		synchronized (lock) {
			return username;
		}
	}

	/**
	 * This method adds a FTPCommunicationListener to the object.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addCommunicationListener(FTPCommunicationListener listener) {
		synchronized (lock) {
			communicationListeners.add(listener);
			if (communication != null) {
				communication.addCommunicationListener(listener);
			}
		}
	}

	/**
	 * This method removes a FTPCommunicationListener previously added to the
	 * object.
	 * 
	 * @param listener
	 *            The listener to be removed.
	 */
	public void removeCommunicationListener(FTPCommunicationListener listener) {
		synchronized (lock) {
			communicationListeners.remove(listener);
			if (communication != null) {
				communication.removeCommunicationListener(listener);
			}
		}
	}

	/**
	 * This method returns a list with all the FTPCommunicationListener used by
	 * the client.
	 * 
	 * @return A list with all the FTPCommunicationListener used by the client.
	 */
	public FTPCommunicationListener[] getCommunicationListeners() {
		synchronized (lock) {
			int size = communicationListeners.size();
			FTPCommunicationListener[] ret = new FTPCommunicationListener[size];
			for (int i = 0; i < size; i++) {
				ret[i] = (FTPCommunicationListener) communicationListeners
						.get(i);
			}
			return ret;
		}
	}

	/**
	 * This method adds a FTPListParser to the object.
	 * 
	 * @param listParser
	 *            The list parser.
	 */
	public void addListParser(FTPListParser listParser) {
		synchronized (lock) {
			listParsers.add(listParser);
		}
	}

	/**
	 * This method removes a FTPListParser previously added to the object.
	 * 
	 * @param listParser
	 *            The list parser to be removed.
	 */
	public void removeListParser(FTPListParser listParser) {
		synchronized (lock) {
			listParsers.remove(listParser);
		}
	}

	/**
	 * This method returns a list with all the FTPListParser used by the client.
	 * 
	 * @return A list with all the FTPListParser used by the client.
	 */
	public FTPListParser[] getListParsers() {
		synchronized (lock) {
			int size = listParsers.size();
			FTPListParser[] ret = new FTPListParser[size];
			for (int i = 0; i < size; i++) {
				ret[i] = (FTPListParser) listParsers.get(i);
			}
			return ret;
		}
	}

	/**
	 * This method connects the client to the remote FTP host, using the default
	 * port value 21.
	 * 
	 * @param host
	 *            The hostname of the remote server.
	 * @return The server welcome message, one line per array element.
	 * @throws IllegalStateException
	 *             If the client is already connected to a remote host.
	 * @throws IOException
	 *             If an I/O occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the server refuses the connection.
	 */
	public String[] connect(String host) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		return connect(host, 21);
	}

	/**
	 * This method connects the client to the remote FTP host.
	 * 
	 * @param host
	 *            The host name or address of the remote server.
	 * @param port
	 *            The port listened by the remote server.
	 * @return The server welcome message, one line per array element.
	 * @throws IllegalStateException
	 *             If the client is already connected to a remote host.
	 * @throws IOException
	 *             If an I/O occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the server refuses the connection.
	 */
	public String[] connect(String host, int port)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client already connected to any host?
			if (connected) {
				throw new IllegalStateException("Client already connected to "
						+ host + " on port " + port);
			}
			// Ok, it's connection time. Let's try!
			FTPConnection connection = null;
			try {
				// Open the connection.
				connection = connector.connectForCommunicationChannel(host,
						port);
				// Open the communication channel.
				communication = new FTPCommunicationChannel(connection);
				for (Iterator i = communicationListeners.iterator(); i
						.hasNext();) {
					communication
							.addCommunicationListener((FTPCommunicationListener) i
									.next());
				}
				// Welcome message.
				FTPReply wm = communication.readFTPReply();
				// Does this reply mean "ok"?
				if (wm.getCode() != 220) {
					// Mmmmm... it seems no!
					throw new FTPException(wm);
				}
				// Flag this object as connected to the remote host.
				connected = true;
				authenticated = false;
				parser = null;
				this.host = host;
				this.port = port;
				this.username = null;
				this.password = null;
				return wm.getMessages();
			} catch (IOException e) {
				// D'oh!
				throw e;
			} finally {
				// If connection has failed...
				if (!connected) {
					if (connection != null) {
						// Close the connection, 'cause it should be open.
						try {
							connection.close();
						} catch (Throwable t) {
							;
						}
					}
				}
			}
		}
	}

	/**
	 * This method disconnects from the remote server, optionally performing the
	 * QUIT procedure.
	 * 
	 * @param sendQuitCommand
	 *            If true the QUIT procedure with the server will be performed,
	 *            otherwise the connection is abruptly closed by the client
	 *            without sending any advice to the server.
	 * @throws IllegalStateException
	 *             If the client is not connected to a remote host.
	 * @throws IOException
	 *             If an I/O occurs (can be thrown only if sendQuitCommand is
	 *             true).
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way (can be thrown only
	 *             if sendQuitCommand is true).
	 * @throws FTPException
	 *             If the server refuses the QUIT command (can be thrown only if
	 *             sendQuitCommand is true).
	 */
	public void disconnect(boolean sendQuitCommand)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			if (sendQuitCommand) {
				// Call the QUIT command.
				communication.sendFTPCommand("QUIT");
				FTPReply r = communication.readFTPReply();
				if (r.getCode() != 221) {
					throw new FTPException(r);
				}
			}
			// Close the communication.
			communication.close();
			communication = null;
			// Reset the connection flag.
			connected = false;
		}
	}

	/**
	 * This method authenticates the user against the server.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password (if none set it to null).
	 * @throws IllegalStateException
	 *             If the client is not connected. Call the connect() method
	 *             before!
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If login fails.
	 */
	public void login(String username, String password)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		login(username, password, null);
	}

	/**
	 * This method authenticates the user against the server.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password (if none set it to null).
	 * @param account
	 *            The account (if none set it to null). Be carefull: some
	 *            servers don't implement this feature.
	 * @throws IllegalStateException
	 *             If the client is not connected. Call the connect() method
	 *             before!
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If login fails.
	 */
	public void login(String username, String password, String account)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Reset the authentication flag.
			authenticated = false;
			// Usefull flags.
			boolean passwordRequired;
			boolean accountRequired;
			// Send the user and read the reply.
			communication.sendFTPCommand("USER " + username);
			FTPReply r = communication.readFTPReply();
			switch (r.getCode()) {
			case 230:
				// Password and account aren't required.
				passwordRequired = false;
				accountRequired = false;
				break;
			case 331:
				// Password is required.
				passwordRequired = true;
				// Account... maybe! More information later...
				accountRequired = false;
				break;
			case 332:
				// Password is not required, but account is required.
				passwordRequired = false;
				accountRequired = true;
			default:
				// User validation failed.
				throw new FTPException(r);
			}
			// Password.
			if (passwordRequired) {
				if (password == null) {
					throw new FTPException(331);
				}
				// Send the password.
				communication.sendFTPCommand("PASS " + password);
				r = communication.readFTPReply();
				switch (r.getCode()) {
				case 230:
					// Account is not required.
					accountRequired = false;
					break;
				case 332:
					// Account is required.
					accountRequired = true;
					break;
				default:
					// Authentication failed.
					throw new FTPException(r);
				}
			}
			// Account.
			if (accountRequired) {
				if (account == null) {
					throw new FTPException(332);
				}
				// Send the account.
				communication.sendFTPCommand("ACCT " + account);
				r = communication.readFTPReply();
				switch (r.getCode()) {
				case 230:
					// Weel done!
					break;
				default:
					// Something goes wrong.
					throw new FTPException(r);
				}
			}
			// Well, if this point is reached the client could consider itself
			// as authenticated.
			this.authenticated = true;
			this.username = username;
			this.password = password;
		}
	}

	/**
	 * This method performs a logout operation for the current user, leaving the
	 * connection open, thus it can be used to start a new user session. Be
	 * carefull with this: some FTP servers don't implement this feature, even
	 * though it is a standard FTP one.
	 * 
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void logout() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the REIN command.
			communication.sendFTPCommand("REIN");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 220) {
				throw new FTPException(r);
			} else {
				// Ok. Not authenticated, now.
				authenticated = false;
				username = null;
				password = null;
			}
		}
	}

	/**
	 * This method performs a "noop" operation with the server.
	 * 
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If login fails.
	 */
	public void noop() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the noop.
			communication.sendFTPCommand("NOOP");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 200) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method sends a custom command to the server. Don't use this method
	 * to send standard commands already supported by the client: this should
	 * cause unexepected results.
	 * 
	 * @param command
	 *            The command line.
	 * @return The reply supplied by the server, parsed and served in an object
	 *         way mode.
	 * @throws IllegalStateException
	 *             If this client is not connected.
	 * @throws IOException
	 *             If a I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 */
	public FTPReply sendCustomCommand(String command)
			throws IllegalStateException, IOException, FTPIllegalReplyException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Send the command and return the reply.
			communication.sendFTPCommand(command);
			return communication.readFTPReply();
		}
	}

	/**
	 * This method sends a SITE specific command to the server.
	 * 
	 * @param command
	 *            The site command.
	 * @return The reply supplied by the server, parsed and served in an object
	 *         way mode.
	 * @throws IllegalStateException
	 *             If this client is not connected.
	 * @throws IOException
	 *             If a I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 */
	public FTPReply sendSiteCommand(String command)
			throws IllegalStateException, IOException, FTPIllegalReplyException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Send the command and return the reply.
			communication.sendFTPCommand("SITE " + command);
			return communication.readFTPReply();
		}
	}

	/**
	 * Call this method to switch the user current account. Be carefull with
	 * this: some FTP servers don't implement this feature, even though it is a
	 * standard FTP one.
	 * 
	 * @param account
	 *            The account.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If login fails.
	 */
	public void changeAccount(String account) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the ACCT command.
			communication.sendFTPCommand("ACCT " + account);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 230) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method asks and returns the current working directory.
	 * 
	 * @return path The path to the current working directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public String currentDirectory() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the PWD command.
			communication.sendFTPCommand("PWD");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 257) {
				throw new FTPException(r);
			}
			// Parse the response.
			String[] messages = r.getMessages();
			if (messages.length != 1) {
				throw new FTPIllegalReplyException();
			}
			Matcher m = PWD_PATTERN.matcher(messages[0]);
			if (m.find()) {
				return messages[0].substring(m.start() + 1, m.end() - 1);
			} else {
				throw new FTPIllegalReplyException();
			}
		}
	}

	/**
	 * This method changes the current working directory.
	 * 
	 * @param path
	 *            The path to the new working directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void changeDirectory(String path) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the CWD command.
			communication.sendFTPCommand("CWD " + path);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 250) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method changes the current working directory to the parent one.
	 * 
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void changeDirectoryUp() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the CWD command.
			communication.sendFTPCommand("CDUP");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 250) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method asks and returns the last modification date of a file or
	 * directory.
	 * 
	 * @param path
	 *            The path to the file or the directory.
	 * @return The file/directory last modification date.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public Date modifiedDate(String path) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the MDTM command.
			communication.sendFTPCommand("MDTM " + path);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 213) {
				throw new FTPException(r);
			}
			String[] messages = r.getMessages();
			if (messages.length != 1) {
				throw new FTPIllegalReplyException();
			} else {
				try {
					return MDTM_DATE_FORMAT.parse(messages[0]);
				} catch (ParseException e) {
					throw new FTPIllegalReplyException();
				}
			}
		}
	}

	/**
	 * This method asks and returns a file size in bytes.
	 * 
	 * @param path
	 *            The path to the file.
	 * @return The file size in bytes.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public long fileSize(String path) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the SIZE command.
			communication.sendFTPCommand("SIZE " + path);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 213) {
				throw new FTPException(r);
			}
			String[] messages = r.getMessages();
			if (messages.length != 1) {
				throw new FTPIllegalReplyException();
			} else {
				try {
					return Long.parseLong(messages[0]);
				} catch (Throwable t) {
					throw new FTPIllegalReplyException();
				}
			}
		}
	}

	/**
	 * This method renames a remote file or directory. It can also be used to
	 * move a file or a directory.
	 * 
	 * In example:
	 * 
	 * <pre>
	 * client.rename(&quot;oldname&quot;, &quot;newname&quot;); // This one renames
	 * </pre>
	 * 
	 * <pre>
	 * client.rename(&quot;the/old/path/oldname&quot;, &quot;/a/new/path/newname&quot;); // This one moves
	 * </pre>
	 * 
	 * @param oldPath
	 *            The current path of the file (or directory).
	 * @param newPath
	 *            The new path for the file (or directory).
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void rename(String oldPath, String newPath)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the RNFR command.
			communication.sendFTPCommand("RNFR " + oldPath);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 350) {
				throw new FTPException(r);
			}
			// Send the RNFR command.
			communication.sendFTPCommand("RNTO " + newPath);
			r = communication.readFTPReply();
			if (r.getCode() != 250) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method deletes a remote file.
	 * 
	 * @param path
	 *            The path to the file.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void deleteFile(String path) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the DELE command.
			communication.sendFTPCommand("DELE " + path);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 250) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method deletes a remote directory.
	 * 
	 * @param path
	 *            The path to the directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void deleteDirectory(String path) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the RMD command.
			communication.sendFTPCommand("RMD " + path);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 250) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method creates a new remote directory in the current working one.
	 * 
	 * @param directoryName
	 *            The name of the new directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public void createDirectory(String directoryName)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the MKD command.
			communication.sendFTPCommand("MKD " + directoryName);
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 257) {
				throw new FTPException(r);
			}
		}
	}

	/**
	 * This method calls the HELP command on the remote server, returning a list
	 * of lines with the help contents.
	 * 
	 * @return The help contents, splitted by line.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public String[] help() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the HELP command.
			communication.sendFTPCommand("HELP");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 214) {
				throw new FTPException(r);
			}
			return r.getMessages();
		}
	}

	/**
	 * This method returns the remote server status, as the result of a FTP STAT
	 * command.
	 * 
	 * @return The remote server status, splitted by line.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 */
	public String[] serverStatus() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// Send the STAT command.
			communication.sendFTPCommand("STAT");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 211) {
				throw new FTPException(r);
			}
			return r.getMessages();
		}
	}

	/**
	 * This method lists the entries of the current working directory parsing
	 * the reply to a FTP LIST command.
	 * 
	 * The response to the LIST command is parsed through the FTPListParser
	 * objects registered on the client. The distribution of ftp4j contains some
	 * standard parsers already registered on every FTPClient object created. If
	 * they don't work in your case (a FTPListParseException is thrown), you can
	 * build your own parser implementing the FTPListParser interface and add it
	 * to the client by calling its addListParser() method.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The list() method will break with a
	 * FTPAbortedException.
	 * 
	 * @return The list of the files (and directories) in the current working
	 *         directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @throws FTPListParseException
	 *             If none of the registered parsers can handle the response
	 *             sent by the server.
	 * @see FTPListParser
	 * @see FTPClient#addListParser(FTPListParser)
	 * @see FTPClient#getListParsers()
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 * @see FTPClient#listNames()
	 */
	public FTPFile[] list() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException, FTPListParseException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// The connection for the data transfer.
			FTPConnection dtConnection;
			// Active or passive?
			if (passive) {
				dtConnection = openPassiveDataTransferChannel();
			} else {
				dtConnection = openActiveDataTransferChannel();
			}
			// ASCII, please!
			communication.sendFTPCommand("TYPE A");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 200) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Send the list command.
			communication.sendFTPCommand("LIST");
			r = communication.readFTPReply();
			if (r.getCode() != 150) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Change the operation status.
			synchronized (abortLock) {
				ongoingDataTransfer = true;
				aborted = false;
			}
			dataTransferInputStream = dtConnection.getInputStream();
			// Fetch the list from the data transfer connection.
			ArrayList lines = new ArrayList();
			NTVASCIIReader dataReader = null;
			try {
				dataReader = new NTVASCIIReader(dataTransferInputStream);
				String line;
				while ((line = dataReader.readLine()) != null) {
					if (line.length() > 0) {
						lines.add(line);
					}
				}
			} catch (IOException e) {
				synchronized (abortLock) {
					if (aborted) {
						throw new FTPAbortedException();
					} else {
						throw new FTPDataTransferException(
								"I/O error in data transfer", e);
					}
				}
			} finally {
				if (dataReader != null) {
					try {
						dataReader.close();
					} catch (Throwable t) {
						;
					}
				}
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				// Consume the result reply of the transfer.
				communication.readFTPReply();
				// Set to null the instance-level input stream.
				dataTransferInputStream = null;
				// Change the operation status.
				synchronized (abortLock) {
					ongoingDataTransfer = false;
					aborted = false;
				}
			}
			// Build an array of lines.
			int size = lines.size();
			String[] list = new String[size];
			for (int i = 0; i < size; i++) {
				list[i] = (String) lines.get(i);
			}
			// Parse the list.
			FTPFile[] ret = null;
			if (parser == null) {
				// Try to parse the list with every parser available.
				for (Iterator i = listParsers.iterator(); i.hasNext();) {
					FTPListParser aux = (FTPListParser) i.next();
					try {
						// Let's try!
						ret = aux.parse(list);
						// This parser smells good!
						parser = aux;
						// Leave the loop.
						break;
					} catch (FTPListParseException e) {
						// Let's try the next one.
						continue;
					}
				}
			} else {
				ret = parser.parse(list);
			}
			if (ret == null) {
				// None of the parsers can handle the list response.
				throw new FTPListParseException();
			} else {
				// Return the parsed list.
				return ret;
			}
		}
	}

	/**
	 * This method lists the entries of the current working directory with a FTP
	 * NLST command.
	 * 
	 * The response consists in an array of string, each one reporting the name
	 * of a file or a directory placed in the current working directory. For a
	 * more detailed directory listing procedure look at the list() method.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The listNames() method will break with a
	 * FTPAbortedException.
	 * 
	 * @return The list of the files (and directories) in the current working
	 *         directory.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @throws FTPListParseException
	 *             If none of the registered parsers can handle the response
	 *             sent by the server.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 * @see FTPClient#list()
	 */
	public String[] listNames() throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException, FTPListParseException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// The connection for the data transfer.
			FTPConnection dtConnection;
			// Active or passive?
			if (passive) {
				dtConnection = openPassiveDataTransferChannel();
			} else {
				dtConnection = openActiveDataTransferChannel();
			}
			// ASCII, please!
			communication.sendFTPCommand("TYPE A");
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 200) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Send the NLST command.
			communication.sendFTPCommand("NLST");
			r = communication.readFTPReply();
			if (r.getCode() != 150) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Change the operation status.
			synchronized (abortLock) {
				ongoingDataTransfer = true;
				aborted = false;
			}
			dataTransferInputStream = dtConnection.getInputStream();
			// Fetch the list from the data transfer connection.
			ArrayList lines = new ArrayList();
			NTVASCIIReader dataReader = null;
			try {
				dataReader = new NTVASCIIReader(dataTransferInputStream);
				String line;
				while ((line = dataReader.readLine()) != null) {
					if (line.length() > 0) {
						lines.add(line);
					}
				}
			} catch (IOException e) {
				synchronized (abortLock) {
					if (aborted) {
						throw new FTPAbortedException();
					} else {
						throw new FTPDataTransferException(
								"I/O error in data transfer", e);
					}
				}
			} finally {
				if (dataReader != null) {
					try {
						dataReader.close();
					} catch (Throwable t) {
						;
					}
				}
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				// Consume the result reply of the transfer.
				communication.readFTPReply();
				// Set to null the instance-level input stream.
				dataTransferInputStream = null;
				// Change the operation status.
				synchronized (abortLock) {
					ongoingDataTransfer = false;
					aborted = false;
				}
			}
			// Build an array.
			int size = lines.size();
			String[] list = new String[size];
			for (int i = 0; i < size; i++) {
				list[i] = (String) lines.get(i);
			}
			return list;
		}
	}

	/**
	 * This method uploads a file to the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param file
	 *            The file to upload.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void upload(File file) throws IllegalStateException,
			FileNotFoundException, IOException, FTPIllegalReplyException,
			FTPException, FTPDataTransferException, FTPAbortedException {
		upload(file, 0, null);
	}

	/**
	 * This method uploads a file to the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param file
	 *            The file to upload.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void upload(File file, FTPDataTransferListener listener)
			throws IllegalStateException, FileNotFoundException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException {
		upload(file, 0, listener);
	}

	/**
	 * This method resumes an upload of a file to the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param file
	 *            The file to upload.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded).
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void upload(File file, long restartAt) throws IllegalStateException,
			FileNotFoundException, IOException, FTPIllegalReplyException,
			FTPException, FTPDataTransferException, FTPAbortedException {
		upload(file, restartAt, null);
	}

	/**
	 * This method resumes an upload of a file to the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param file
	 *            The file to upload.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded).
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void upload(File file, long restartAt,
			FTPDataTransferListener listener) throws IllegalStateException,
			FileNotFoundException, IOException, FTPIllegalReplyException,
			FTPException, FTPDataTransferException, FTPAbortedException {
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (IOException e) {
			throw new FTPDataTransferException(e);
		}
		try {
			upload(file.getName(), inputStream, restartAt, restartAt, file
					.length(), listener);
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (FTPIllegalReplyException e) {
			throw e;
		} catch (FTPException e) {
			throw e;
		} catch (FTPDataTransferException e) {
			throw e;
		} catch (FTPAbortedException e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

	/**
	 * This method resumes an upload to the remote server.
	 * 
	 * Note that no byte is skipped in the given inputStream. The restartAt
	 * value is, in this case, an information for the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param fileName
	 *            The name of the remote file.
	 * @param inputStream
	 *            The source of data.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded).
	 * @param streamOffset
	 *            The offset to skip in the stream.
	 * @param streamLength
	 *            The length of the data of the stream to upload.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void upload(String fileName, InputStream inputStream,
			long restartAt, long streamOffset, long streamLength,
			FTPDataTransferListener listener) throws IllegalStateException,
			IOException, FTPIllegalReplyException, FTPException,
			FTPDataTransferException, FTPAbortedException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// The connection for the data transfer.
			FTPConnection dtConnection;
			// Active or passive?
			if (passive) {
				dtConnection = openPassiveDataTransferChannel();
			} else {
				dtConnection = openActiveDataTransferChannel();
			}
			// Select the type of contents.
			int tp = type;
			if (tp == TYPE_AUTO) {
				tp = detectType(fileName);
			}
			if (tp == TYPE_TEXTUAL) {
				communication.sendFTPCommand("TYPE A");
			} else if (tp == TYPE_BINARY) {
				communication.sendFTPCommand("TYPE I");
			}
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 200) {
				throw new FTPException(r);
			}
			// Send the REST command.
			communication.sendFTPCommand("REST " + restartAt);
			r = communication.readFTPReply();
			if (r.getCode() != 350) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Send the STOR command.
			communication.sendFTPCommand("STOR " + fileName);
			r = communication.readFTPReply();
			if (r.getCode() != 150) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Change the operation status.
			synchronized (abortLock) {
				ongoingDataTransfer = true;
				aborted = false;
			}
			dataTransferOutputStream = dtConnection.getOutputStream();
			// Upload the stream.
			long done = 0;
			try {
				inputStream.skip(streamOffset);
				if (listener != null) {
					listener.started();
				}
				if (tp == TYPE_TEXTUAL) {
					Reader reader = new InputStreamReader(inputStream);
					Writer writer = new OutputStreamWriter(
							dataTransferOutputStream, "UTF-8");
					char[] buffer = new char[1024];
					while (done < streamLength) {
						int l = reader.read(buffer, 0, (int) Math.min(
								buffer.length, streamLength - done));
						if (l == -1) {
							throw new IOException("End of stream reached");
						} else {
							writer.write(buffer, 0, l);
							done += l;
							if (listener != null) {
								listener.transferred(l);
							}
						}
					}
				} else if (tp == TYPE_BINARY) {
					byte[] buffer = new byte[1024];
					while (done < streamLength) {
						int l = inputStream.read(buffer, 0, (int) Math.min(
								buffer.length, streamLength - done));
						if (l == -1) {
							throw new IOException("End of stream reached");
						} else {
							dataTransferOutputStream.write(buffer, 0, l);
							done += l;
							if (listener != null) {
								listener.transferred(l);
							}
						}
					}
				}
			} catch (IOException e) {
				synchronized (abortLock) {
					if (aborted) {
						if (listener != null) {
							listener.aborted();
						}
						throw new FTPAbortedException();
					} else {
						if (listener != null) {
							listener.failed();
						}
						throw new FTPDataTransferException(
								"I/O error in data transfer", e);
					}
				}
			} finally {
				if (dataTransferOutputStream != null) {
					try {
						dataTransferOutputStream.close();
					} catch (Throwable t) {
						;
					}
				}
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				// Set to null the instance-level input stream.
				dataTransferOutputStream = null;
				// Consume the result reply of the transfer.
				communication.readFTPReply();
				// Change the operation status.
				synchronized (abortLock) {
					ongoingDataTransfer = false;
					aborted = false;
				}
			}
			if (listener != null) {
				listener.completed();
			}
		}
	}

	/**
	 * This method downloads a remote file from the server to a local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void download(String remoteFileName, File localFile)
			throws IllegalStateException, FileNotFoundException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException {
		download(remoteFileName, localFile, 0, null);
	}

	/**
	 * This method downloads a remote file from the server to a local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void download(String remoteFileName, File localFile,
			FTPDataTransferListener listener) throws IllegalStateException,
			FileNotFoundException, IOException, FTPIllegalReplyException,
			FTPException, FTPDataTransferException, FTPAbortedException {
		download(remoteFileName, localFile, 0, listener);
	}

	/**
	 * This method resumes a download operation from the remote server to a
	 * local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded).
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void download(String remoteFileName, File localFile, long restartAt)
			throws IllegalStateException, FileNotFoundException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException {
		download(remoteFileName, localFile, restartAt, null);
	}

	/**
	 * This method resumes a download operation from the remote server to a
	 * local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded).
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws FileNotFoundException
	 *             If the supplied file cannot be found.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void download(String remoteFileName, File localFile, long restartAt,
			FTPDataTransferListener listener) throws IllegalStateException,
			FileNotFoundException, IOException, FTPIllegalReplyException,
			FTPException, FTPDataTransferException, FTPAbortedException {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(localFile, true);
		} catch (IOException e) {
			throw new FTPDataTransferException(e);
		}
		try {
			download(remoteFileName, outputStream, restartAt, listener);
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (FTPIllegalReplyException e) {
			throw e;
		} catch (FTPException e) {
			throw e;
		} catch (FTPDataTransferException e) {
			throw e;
		} catch (FTPAbortedException e) {
			throw e;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

	/**
	 * This method resumes a download operation from the remote server.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with a
	 * FTPAbortedException.
	 * 
	 * @param fileName
	 *            The name of the remote file.
	 * @param outputStream
	 *            The destination stream of data read during the download.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded).
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws IllegalStateException
	 *             If the client is not connected or not authenticated.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws FTPIllegalReplyException
	 *             If the server replies in an illegal way.
	 * @throws FTPException
	 *             If the operation fails.
	 * @throws FTPDataTransferException
	 *             If a I/O occurs in the data transfer connection. If you
	 *             receive this exception the trasfer failed, but the main
	 *             connection with the remote FTP server is in theory still
	 *             working.
	 * @throws FTPAbortedException
	 *             If operation is aborted by another thread.
	 * @see FTPClient#abortCurrentDataTransfer(boolean)
	 */
	public void download(String fileName, OutputStream outputStream,
			long restartAt, FTPDataTransferListener listener)
			throws IllegalStateException, IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException,
			FTPAbortedException {
		synchronized (lock) {
			// Is this client connected?
			if (!connected) {
				throw new IllegalStateException("Client not connected");
			}
			// Is this client authenticated?
			if (!authenticated) {
				throw new IllegalStateException("Client not authenticated");
			}
			// The connection for the data transfer.
			FTPConnection dtConnection;
			// Active or passive?
			if (passive) {
				dtConnection = openPassiveDataTransferChannel();
			} else {
				dtConnection = openActiveDataTransferChannel();
			}
			// Select the type of contents.
			int tp = type;
			if (tp == TYPE_AUTO) {
				tp = detectType(fileName);
			}
			if (tp == TYPE_TEXTUAL) {
				communication.sendFTPCommand("TYPE A");
			} else if (tp == TYPE_BINARY) {
				communication.sendFTPCommand("TYPE I");
			}
			FTPReply r = communication.readFTPReply();
			if (r.getCode() != 200) {
				throw new FTPException(r);
			}
			// Send the REST command.
			communication.sendFTPCommand("REST " + restartAt);
			r = communication.readFTPReply();
			if (r.getCode() != 350) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Send the RETR command.
			communication.sendFTPCommand("RETR " + fileName);
			r = communication.readFTPReply();
			if (r.getCode() != 150) {
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				throw new FTPException(r);
			}
			// Change the operation status.
			synchronized (abortLock) {
				ongoingDataTransfer = true;
				aborted = false;
			}
			dataTransferInputStream = dtConnection.getInputStream();
			// Download the stream.
			try {
				if (listener != null) {
					listener.started();
				}
				if (tp == TYPE_TEXTUAL) {
					Reader reader = new InputStreamReader(
							dataTransferInputStream, "UTF-8");
					Writer writer = new OutputStreamWriter(outputStream);
					char[] buffer = new char[1024];
					int l;
					while ((l = reader.read(buffer, 0, buffer.length)) != -1) {
						writer.write(buffer, 0, l);
						if (listener != null) {
							listener.transferred(l);
						}
					}
				} else if (tp == TYPE_BINARY) {
					byte[] buffer = new byte[1024];
					int l;
					while ((l = dataTransferInputStream.read(buffer, 0,
							buffer.length)) != -1) {
						outputStream.write(buffer, 0, l);
						if (listener != null) {
							listener.transferred(l);
						}
					}
				}
			} catch (IOException e) {
				synchronized (abortLock) {
					if (aborted) {
						if (listener != null) {
							listener.aborted();
						}
						throw new FTPAbortedException();
					} else {
						if (listener != null) {
							listener.failed();
						}
						throw new FTPDataTransferException(
								"I/O error in data transfer", e);
					}
				}
			} finally {
				if (dataTransferInputStream != null) {
					try {
						dataTransferInputStream.close();
					} catch (Throwable t) {
						;
					}
				}
				try {
					dtConnection.close();
				} catch (Throwable t) {
					;
				}
				// Set to null the instance-level input stream.
				dataTransferInputStream = null;
				// Consume the result reply of the transfer.
				communication.readFTPReply();
				// Change the operation status.
				synchronized (abortLock) {
					ongoingDataTransfer = false;
					aborted = false;
				}
			}
			if (listener != null) {
				listener.completed();
			}
		}
	}

	/**
	 * This method detects the type for a file transfer.
	 */
	private int detectType(String fileName) throws IOException,
			FTPIllegalReplyException, FTPException {
		int start = fileName.lastIndexOf('.');
		int stop = fileName.length();
		if (start > 0 && start < stop - 1) {
			String ext = fileName.substring(start, stop);
			ext = ext.toLowerCase();
			if (textualExtensionRecognizer.isTextualExt(ext)) {
				return TYPE_TEXTUAL;
			} else {
				return TYPE_BINARY;
			}
		} else {
			return TYPE_BINARY;
		}
	}

	/**
	 * This method opens a data transfer channel in active mode.
	 */
	private FTPConnection openActiveDataTransferChannel() throws IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException {
		// Create a FTPDataTransferServer object.
		FTPDataTransferServer server = new FTPDataTransferServer();
		int port = server.getPort();
		int p1 = port >>> 8;
		int p2 = port & 0xff;
		byte[] addr = InetAddress.getLocalHost().getAddress();
		int b1 = addr[0] & 0xff;
		int b2 = addr[1] & 0xff;
		int b3 = addr[2] & 0xff;
		int b4 = addr[3] & 0xff;
		// Send the port command.
		communication.sendFTPCommand("PORT " + b1 + "," + b2 + "," + b3 + ","
				+ b4 + "," + p1 + "," + p2);
		FTPReply r = communication.readFTPReply();
		if (r.getCode() != 200) {
			server.close();
			throw new FTPException(r);
		}
		return server.getConnection();
	}

	/**
	 * This method opens a data transfer channel in passive mode.
	 */
	private FTPConnection openPassiveDataTransferChannel() throws IOException,
			FTPIllegalReplyException, FTPException, FTPDataTransferException {
		// Send the PASV command.
		communication.sendFTPCommand("PASV");
		// Read the reply.
		FTPReply r = communication.readFTPReply();
		if (r.getCode() != 227) {
			throw new FTPException(r);
		}
		// Use a regexp to extract the remote address and port.
		String addressAndPort = null;
		String[] messages = r.getMessages();
		for (int i = 0; i < messages.length; i++) {
			Matcher m = PASV_PATTERN.matcher(messages[i]);
			if (m.find()) {
				int start = m.start();
				int end = m.end();
				addressAndPort = messages[i].substring(start, end);
				break;
			}
		}
		if (addressAndPort == null) {
			// The remote server has not sent the coordinates for the
			// data transfer connection.
			throw new FTPIllegalReplyException();
		}
		// Parse the string extracted from the reply.
		StringTokenizer st = new StringTokenizer(addressAndPort, ",");
		int b1 = Integer.parseInt(st.nextToken());
		int b2 = Integer.parseInt(st.nextToken());
		int b3 = Integer.parseInt(st.nextToken());
		int b4 = Integer.parseInt(st.nextToken());
		int p1 = Integer.parseInt(st.nextToken());
		int p2 = Integer.parseInt(st.nextToken());
		InetAddress remoteAddress = InetAddress.getByAddress(new byte[] {
				(byte) b1, (byte) b2, (byte) b3, (byte) b4 });
		int remotePort = (p1 << 8) | p2;
		// Establish the connection.
		final FTPConnection dtConnection;
		try {
			dtConnection = connector.connectForDataTransferChannel(
					remoteAddress.getHostAddress(), remotePort);
		} catch (IOException e) {
			throw new FTPDataTransferException(
					"Cannot connect to the remote server", e);
		}
		return dtConnection;
	}

	/**
	 * If there's any ongoing data transfer operation, this method aborts it.
	 * 
	 * @param sendAborCommand
	 *            If true the client will negotiate the abort procedure with the
	 *            server, through the standard FTP ABOR command. Otherwise the
	 *            open data transfer connection will be closed one-sideness
	 *            without any advise has sent to the server.
	 * @throws IOException
	 *             If the ABOR command cannot be sent due to any I/O error. This
	 *             could happen only if force is false.
	 * @throws FTPIllegalReplyException
	 *             If the server reply to the ABOR command is illegal. This
	 *             could happen only if force is false.
	 */
	public void abortCurrentDataTransfer(boolean sendAborCommand)
			throws IOException, FTPIllegalReplyException {
		synchronized (abortLock) {
			if (ongoingDataTransfer && !aborted) {
				if (sendAborCommand) {
					communication.sendFTPCommand("ABOR");
					communication.readFTPReply();
				}
				if (dataTransferInputStream != null) {
					try {
						dataTransferInputStream.close();
					} catch (Throwable t) {
						;
					}
				}
				if (dataTransferOutputStream != null) {
					try {
						dataTransferOutputStream.close();
					} catch (Throwable t) {
						;
					}
				}
				aborted = true;
			}
		}
	}

	public String toString() {
		synchronized (lock) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(getClass().getName());
			buffer.append(" [connected=");
			buffer.append(connected);
			if (connected) {
				buffer.append(", host=");
				buffer.append(host);
				buffer.append(", port=");
				buffer.append(port);
			}
			buffer.append(", authenticated=");
			buffer.append(authenticated);
			if (authenticated) {
				buffer.append(", username=");
				buffer.append(username);
				buffer.append(", password=");
				StringBuffer buffer2 = new StringBuffer();
				for (int i = 0; i < password.length(); i++) {
					buffer2.append('*');
				}
				buffer.append(buffer2);
			}
			buffer.append(", transfer mode=");
			buffer.append(passive ? "passive" : "active");
			buffer.append(", transfer type=");
			switch (type) {
			case TYPE_AUTO:
				buffer.append("auto");
				break;
			case TYPE_BINARY:
				buffer.append("binary");
				break;
			case TYPE_TEXTUAL:
				buffer.append("textual");
				break;
			}
			buffer.append(", connector=");
			buffer.append(connector);
			buffer.append(", textualExtensionRecognizer=");
			buffer.append(textualExtensionRecognizer);
			FTPListParser[] listParsers = getListParsers();
			if (listParsers.length > 0) {
				buffer.append(", listParsers=");
				for (int i = 0; i < listParsers.length; i++) {
					if (i > 0) {
						buffer.append(", ");
					}
					buffer.append(listParsers[i]);
				}
			}
			FTPCommunicationListener[] communicationListeners = getCommunicationListeners();
			if (communicationListeners.length > 0) {
				buffer.append(", communicationListeners=");
				for (int i = 0; i < communicationListeners.length; i++) {
					if (i > 0) {
						buffer.append(", ");
					}
					buffer.append(communicationListeners[i]);
				}
			}
			buffer.append("]");
			return buffer.toString();
		}
	}

}
