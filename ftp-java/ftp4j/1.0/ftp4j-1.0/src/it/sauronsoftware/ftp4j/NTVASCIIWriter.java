package it.sauronsoftware.ftp4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * This is an NTV-ASCII character stream writer.
 * 
 * @author Carlo Pelliccia
 */
class NTVASCIIWriter extends Writer {

	/**
	 * NTV line separator.
	 */
	private static final String LINE_SEPARATOR = "\r\n";

	/**
	 * The underlying writer.
	 */
	private Writer writer;

	/**
	 * Builds the writer.
	 * 
	 * @param stream
	 *            The underlying stream.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public NTVASCIIWriter(OutputStream stream) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
	}

	public void close() throws IOException {
		writer.close();
	}

	public void flush() throws IOException {
		writer.flush();
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
	}

	/**
	 * Writes a line in the stream.
	 * 
	 * @param str
	 *            The line.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void writeLine(String str) throws IOException {
		StringBuffer buffer = new StringBuffer();
		boolean atLeastOne = false;
		StringTokenizer st = new StringTokenizer(str, LINE_SEPARATOR);
		int count = st.countTokens();
		for (int i = 0; i < count; i++) {
			String line = st.nextToken();
			if (line.length() > 0) {
				if (atLeastOne) {
					buffer.append('\r');
					buffer.append((char) 0);
				}
				buffer.append(line);
				atLeastOne = true;
			}
		}
		if (buffer.length() > 0) {
			String statement = buffer.toString();
			// Sends the statement to the server.
			writer.write(statement);
			writer.write(LINE_SEPARATOR);
			writer.flush();
		}
	}

}
