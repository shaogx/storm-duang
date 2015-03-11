/**
 * shaogx
 */

package storm.util.beanstalk;

public class ClientCli {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String	host = args[0];
		int		port = Integer.parseInt(args[1]);
		
		String tube = args[2];
		String message = args[3];
		
		Client client = new Client(host, port);
		
		client.use(tube);
		client.put(message.getBytes());

	}

}
