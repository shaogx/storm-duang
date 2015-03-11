/**
 * shaogx
 */

package storm.util.beanstalk;

public class Job {

	private long id;
	private byte[] data;	
	
	public Job(long id, byte[] data) {
		this.id		= id;
		this.data	= data;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public long getId() {
		return id;
	}
	
}
