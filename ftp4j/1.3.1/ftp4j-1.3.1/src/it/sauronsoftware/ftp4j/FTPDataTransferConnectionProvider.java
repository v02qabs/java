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
package it.sauronsoftware.ftp4j;

/**
 * A package reserved {@link FTPConnection} provider, used internally by the
 * client to obtain connections for data transfer purposes.
 * 
 * @author cpelliccia
 * 
 */
interface FTPDataTransferConnectionProvider {

	/**
	 * Returns the connection.
	 * 
	 * @return The connection.
	 * @throws FTPException
	 *             If an unexpected error occurs.
	 */
	public FTPConnection openDataTransferConnection()
			throws FTPDataTransferException;

	/**
	 * Terminates the provider.
	 */
	public void dispose();

}