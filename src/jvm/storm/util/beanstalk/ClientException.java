/**
 * shaogx
 */

package storm.util.beanstalk;

public class ClientException extends Exception {

	private static final long serialVersionUID = 1981287084441643194L;
	
	public ClientException () {
		this(null, null);
	}

	public ClientException(String message) {
		this(message, null);
	}

	public ClientException(String message, Exception cause) {
		super(message, cause);
	}

	public ClientException(Exception cause) {
		this(null, cause);
	}
}