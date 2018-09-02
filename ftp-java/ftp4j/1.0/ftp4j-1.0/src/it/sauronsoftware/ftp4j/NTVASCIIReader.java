package it.sauronsoftware.ftp4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This is an NTV-ASCII character stream reader.
 * 
 * @author Carlo Pelliccia
 */
class NTVASCIIReader extends Reader {

	/**
	 * This system line separator chars sequence.
	 */
	private static final String SYSTEM_LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * The underlying reader.
	 */
	private Reader reader;

	/**
	 * Builds the reader.
	 * 
	 * @param stream
	 *            The underlying stream.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public NTVASCIIReader(InputStream stream) throws IOException {
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
	}

	public void close() throws IOException {
		reader.close();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	/**
	 * Reads a line from the stream.
	 * 
	 * @return The line read, or null if the end of the stream is reached.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public String readLine() throws IOException {
		if (true) {
			return ((BufferedReader) reader).readLine();
		}
		StringBuffer buffer = new StringBuffer();
		int previous = -1;
		int current = -1;
		do {
			int i = reader.read();
			if (i == -1) {
				if (buffer.length() == 0) {
					return null;
				} else {
					return buffer.toString();
				}
			}
			previous = current;
			current = i;
			if (previous == '\r' && current == '\n') {
				// End of line.
				String statement = buffer.toString();
				// Return the statement read.
				if (statement.endsWith("Portale RER Shared")) {
					System.out.println(".");
				}
				return statement;
			} else if (previous == '\r' && current == 0) {
				// Literal new line.
				buffer.append(SYSTEM_LINE_SEPARATOR);
			} else if (current != 0 && current != '\r') {
				buffer.append((char) current);
			}
		} while (true);
	}

}
