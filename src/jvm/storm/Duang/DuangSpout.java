/**
 * shaogx
 */

package storm.duang;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import redis.clients.jedis.Jedis;

import storm.duang.beanstalk.BeanstalkClient;
import storm.duang.beanstalk.BeanstalkException;
import storm.duang.beanstalk.BeanstalkJob;
import storm.duang.beanstalk.BeanstalkPool;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

public class DuangSpout extends BaseRichSpout {

	private String _HOST = "172.16.20.113";
	private int _PORT = 11300;

	private Jedis jedis;

	private BeanstalkClient client;
	//private BeanstalkJob job = null;
	private Number bsttr = 5;

	private SpoutOutputCollector collector;

	private boolean completed = false;
	private boolean pool = false;
	private TopologyContext context;

	public boolean isDistributed() {
		return false;
	}

	public void ack(Object jobId) {
		System.out.println("OK:" + jobId);
	}

	public void close() {}

	public void fail(Object jobId) {
		System.out.println("FAIL:" + jobId);
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			//String HOST = conf.get("bsHost").toString();
			//String PORT = conf.get("bsPort").toString();
			//String TUPLE = conf.get("bsTuple").toString();
			//Number bsttr = conf.get("bsTTR").toString();
			//bsttr = 5;

	  		this.collector = collector;
			//this.jedis = new Jedis("172.16.20.113",6379);
			//this.jedis.connect();
			//this.jedis.select(1);

	  		//this.client = new BeanstalkClient(_HOST, _PORT,"logstash");
			//this.client.use("logstash");
		} catch (Exception ex) {
			//ex.printStackTrace();
	 		throw new RuntimeException("Error connecting beanstalk...",ex);
		}
	}

	@Override
	public void nextTuple() {
		while(true){
			
			try{
				this.jedis = new Jedis("172.16.20.113",6379);
				//this.jedis.connect();
				this.jedis.select(1);

				//fetch job as string
				System.out.println("\nfetch job start\n");

				//Utils.sleep(100);

				String job = this.jedis.lpop("logstash");
                                //System.out.println("JobInfo:"+job+"\n");

				//job  = this.client.reserve(60);
				//System.out.println("JobId:"+job.getId()+"\n");
				//System.out.println("JobInfo:"+job.getData()+"\n");
				System.out.println("\nfetch json end\n");
				if(job != null){
					//this.collector.emit(new Values(job.getData()),job.getId());
					String jobId = UUID.randomUUID().toString();
					this.collector.emit(new Values(job), jobId);
				}else{
					Utils.sleep(10000 * 1);
					System.out.println("\nno jobs and sleep 1s...\n");					
				}

				this.jedis.close();

			}catch(Exception ex){
				throw new RuntimeException("Error reading redis...",ex);
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

}
