package cn.itcast.demo.storm.wc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author: Liyuxin wechat:13011800146. @Title: WCTopologyMain @ProjectName gossip_spider_parent
 * @date: 2019/2/13 18:16
 * @description: 组装一个topology, 将topology任务交给storm去执行
 */
public class WCTopologyMain {
    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        // 组装
        TopologyBuilder builder = new TopologyBuilder();
        // 参数1,组件的id 无限制 参数2:spout对象
        builder.setSpout("readFileSpout", new ReadFileSpout());
        // 分组策略:随机分组
        builder.setBolt("splitBolt", new SplitBolt()).shuffleGrouping("readFileSpout");
        builder.setBolt("wordCountBolt", new WordCountBolt()).shuffleGrouping("splitBolt");

        // 提交
        // 提交方式:1.本地 2.集群

        // 设置参数
        Config config = new Config();
        if (args.length > 0 && args != null) {
            //集群,有参数
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            // 本地提交
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wordCount", config, builder.createTopology());
        }
    }
}
