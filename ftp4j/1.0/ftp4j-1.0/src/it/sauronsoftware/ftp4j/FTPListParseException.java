package it.sauronsoftware.ftp4j;

/**
 * Exception thrown by the list() method in FTPClient objects when the response
 * sent by the server to a FTP list command is not parsable through the known
 * parsers.
 * 
 * @author Carlo Pelliccia
 * 
 */
public class FTPListParseException extends Exception {

	private static final long serialVersionUID = 1L;

}
