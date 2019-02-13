package cn.itcast.demo.storm.wc;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: SplitBolt @ProjectName gossip_spider_parent
 * @date: 2019/2/13 17:46
 * @description: 得到上游的数据(句子), 进行切割, 向下游发送单词
 */
public class SplitBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String juzi = input.getStringByField("juzi");
        if (StringUtils.isNotEmpty(juzi)) {
            String[] words = juzi.split(" ");
            // 将单词一个个的向下游发送
            for (String word : words) {
                Values tuple = new Values(word, 1);
                collector.emit(tuple);

                System.out.println(tuple);
            }
        }
    }

    /**
     * Declare the output schema for all the streams of this topology.
     *
     * @param declarer this is used to declare output stream ids, output fields, and whether or not
     *                 each output stream is a direct stream
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "score"));
    }
}
