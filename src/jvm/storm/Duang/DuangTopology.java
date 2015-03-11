/**
 * shaogx
 */

package storm.duang;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.ShellBolt;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Fields;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import storm.duang.DuangSpout;
import storm.duang.DuangBolt;

/**
 * This topology demonstrates Storm's stream groupings and multilang capabilities.
 */
public class DuangTopology {

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("duangin", new DuangSpout(), 6);
		builder.setBolt("duangout", new DuangBolt(),6).fieldsGrouping("duangin", new Fields("line"));

		Config conf = new Config();
		conf.put("bsHost", "172.16.20.113");
		conf.put("bsPORT", 11300);
		conf.put("bsTuple", "logstash");
		conf.put("bsTTR", 5); 

		conf.setDebug(true);
		
		if (args != null && args.length > 0) {
			conf.setNumWorkers(2);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		}
		else {
			conf.setMaxTaskParallelism(8);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("duang", conf, builder.createTopology());

			//Thread.sleep(1000);

			//cluster.killTopology("duang");

			//cluster.shutdown();
		}
	}
}
