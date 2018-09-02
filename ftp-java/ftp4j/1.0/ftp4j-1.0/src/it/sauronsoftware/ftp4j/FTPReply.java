package it.sauronsoftware.ftp4j;

/**
 * This class represents FTP server replies in a manageable object oriented way.
 * 
 * @author Carlo Pelliccia
 */
public class FTPReply {

	/**
	 * The reply code.
	 */
	private int code = 0;

	/**
	 * The reply message(s).
	 */
	private String[] messages;

	/**
	 * Build the reply.
	 * 
	 * @param code
	 *            The code of the reply.
	 * @param message
	 *            The textual message(s) in the reply.
	 */
	FTPReply(int code, String[] messages) {
		this.code = code;
		this.messages = messages;
	}

	/**
	 * Returns the code of the reply.
	 * 
	 * @return The code of the reply.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Returns the textual message(s) of the reply.
	 * 
	 * @return The textual message(s) of the reply.
	 */
	public String[] getMessages() {
		return messages;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName());
		buffer.append(" [code=");
		buffer.append(code);
		buffer.append(", message=");
		for (int i = 0; i < messages.length; i++) {
			if (i > 0) {
				buffer.append(" ");
			}
			buffer.append(messages[i]);
		}
		buffer.append("]");
		return buffer.toString();
	}

}
