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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Base64 encoding and decoding methods for strings.
 * 
 * @author Carlo Pelliccia
 */
public class Base64 {

	/**
	 * Encodes a string as base64.
	 * 
	 * @param str
	 *            Input string.
	 * @return Encoded string.
	 * @throws RuntimeException
	 *             Some error occurred during encoding.
	 */
	public static String encode(String str) throws RuntimeException {
		ByteArrayOutputStream out = null;
		Base64OutputStream base64 = null;
		try {
			out = new ByteArrayOutputStream();
			base64 = new Base64OutputStream(out, 0);
			base64.write(str.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (base64 != null) {
				try {
					base64.close();
				} catch (Throwable t) {
					;
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Throwable t) {
					;
				}
			}
		}
		try {
			return new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decodes a string which is base64 encoded.
	 * 
	 * @param str
	 *            Encoded string.
	 * @return Decoded string.
	 * @throws RuntimeException
	 *             The given string is not well encoded...
	 */
	public static String decode(String str) throws RuntimeException {
		ByteArrayInputStream stream = null;
		Base64InputStream base64 = null;
		Reader reader = null;
		try {
			StringBuffer ret = new StringBuffer();
			byte[] bytes = str.getBytes("ASCII");
			stream = new ByteArrayInputStream(bytes);
			base64 = new Base64InputStream(stream);
			reader = new InputStreamReader(base64, "UTF-8");
			char[] buf = new char[1024];
			int l;
			while ((l = reader.read(buf)) != -1) {
				ret.append(buf, 0, l);
			}
			return ret.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Throwable t) {
					;
				}
			} else if (base64 != null) {
				try {
					base64.close();
				} catch (Throwable t) {
					;
				}
			} else if (stream != null) {
				try {
					stream.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

}
