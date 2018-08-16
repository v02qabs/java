/*
 * ftp4j - A pure Java FTP client library
 * 
 * Copyright (C) 2008 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.sauronsoftware.ftp4j.base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * A base64 encoding input stream.
 * 
 * A Base64InputStream reads from an underlying stream which is supposed to be a
 * base64 encoded stream. Base64InputStream decodes the data read from the
 * underlying stream and returns the decoded bytes to the caller.
 * 
 * @author Carlo Pelliccia
 */
public class Base64InputStream extends InputStream {

	/**
	 * The underlying stream.
	 */
	private InputStream inputStream;

	/**
	 * The buffer.
	 */
	private int[] buffer;

	/**
	 * A counter for values in the buffer.
	 */
	private int bufferCounter = 0;

	/**
	 * End-of-stream flag.
	 */
	private boolean eof = false;

	/**
	 * It builds a base64 decoding input stream.
	 * 
	 * @param inputStream
	 *            The underlying stream, from which the encoded data is read.
	 */
	public Base64InputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public int read() throws IOException {
		if (buffer == null || bufferCounter == buffer.length) {
			if (eof) {
				return -1;
			}
			acquire();
			if (buffer.length == 0) {
				buffer = null;
				return -1;
			}
			bufferCounter = 0;
		}
		return buffer[bufferCounter++];
	}

	/**
	 * Reads from the underlying stream, decodes the data and puts the decoded
	 * bytes into the buffer.
	 */
	private void acquire() throws IOException {
		char[] four = new char[4];
		int i = 0;
		do {
			int b = inputStream.read();
			if (b == -1) {
				if (i != 0) {
					throw new IOException("Bad base64 stream");
				} else {
					buffer = new int[0];
					eof = true;
					return;
				}
			}
			char c = (char) b;
			if (Shared.chars.indexOf(c) != -1 || c == Shared.pad) {
				four[i++] = c;
			}
		} while (i < 4);
		if (four[0] == Shared.pad || four[1] == Shared.pad) {
			throw new IOException("Bad base64 stream");
		}
		if (four[2] == Shared.pad && four[3] != Shared.pad) {
			throw new IOException("Bad base64 stream");
		}
		int l;
		if (four[3] == Shared.pad) {
			if (inputStream.read() != -1) {
				throw new IOException("Bad base64 stream");
			}
			eof = true;
			if (four[2] == Shared.pad) {
				l = 1;
			} else {
				l = 2;
			}
		} else {
			l = 3;
		}
		int aux = 0;
		for (i = 0; i < 4; i++) {
			if (four[i] != Shared.pad) {
				aux = aux | (Shared.chars.indexOf(four[i]) << (6 * (3 - i)));
			}
		}
		buffer = new int[l];
		for (i = 0; i < l; i++) {
			buffer[i] = (aux >>> (8 * (2 - i))) & 0xFF;
		}
	}

	public void close() throws IOException {
		inputStream.close();
	}

}