/**
 * shaogx
 */

package storm.duang;

//import com.alibaba.fastjson.JSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import redis.clients.jedis.Jedis;
import backtype.storm.task.OutputCollector;
//import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DuangBolt extends BaseRichBolt {

	private OutputCollector collector;
	
	Connection conn = null;
	Statement stmt = null;
	String sql;

	String jdbc = "jdbc:mysql://172.16.20.100:3306/test?user=root&password=root@dev";

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void execute(Tuple tuple) {
		System.out.println("\ntuple start\n");
		String line = tuple.getString(0);
		
		try{

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbc);
			} catch (Exception e) {
				System.out.println("\nerror:"+e);
			}


			String request = "";
			long create_at = System.currentTimeMillis()/1000;
			//unsigned int create_at = Math.round(new Date().getTime()/1000);
			String status = "200";
			String dateStr = "";
			long occur_at = 0;

			JSONParser parser=new JSONParser();
			//JSONObject json = new JSONObject();
			try{
				JSONObject json = (JSONObject) parser.parse(line);
				//System.out.println("object"+json);

				//status = (String) json.get("status");
				status = (String) json.get("response");
				request = (String) json.get("request");
				dateStr = (String) json.get("timestamp");
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
				occur_at = (long) sdf.parse(dateStr).getTime()/1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//String  status = "500";
			
			if(!status.equals("200")){
				
				/*
				System.out.println("\noccur_at:"+occur_at);
				System.out.println("\nrequest:"+request);
				System.out.println("\ncreate_at:"+create_at);
				System.out.println("\nstatus:"+status);
				System.out.println("\nline:"+line);
				*/

				String sql = "INSERT INTO `webstats`(`occur_at`,`status`,`request`,`line`,`create_at`) VALUES "
				+ " ("+occur_at+",'"+status+"','"+request+"','"+line+"',"+create_at+");";
				stmt = conn.createStatement();
				//System.out.println("\n"+sql+"\n");
				stmt.execute(sql);
				//stmt.executeUpdate(sql);
			
			}
			this.collector.ack(tuple);
			System.out.println("\ntuple end\n");

			conn.close();

		}catch (Exception ex) {
			System.out.println("xxException: " + ex);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//declarer.declare(new Fields("line"));
	}
	
	@Override
	public void cleanup() {
		//super.cleanup();
		//System.out.println("stats finish!");
	}
}

