package cn.itcast.demo.storm.wc;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: WordCountBolt @ProjectName gossip_spider_parent
 * @date: 2019/2/13 18:08
 * @description: 接收上游发送的单词, 进行数据统计, 打印结果
 */
public class WordCountBolt extends BaseRichBolt {

    // 封装单词和出现次数
    private Map<String, Integer> map;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        map = new HashMap<String, Integer>();
    }

    /**
     * 统计单词出现的次数
     *
     * @param input The input tuple to be processed.
     */
    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("word");

        if (map.get(word) != null) {
            // 该单词存在
            map.put(word, map.get(word) + 1); // 单词次数加1
        } else {
            map.put(word, 1);
        }

        //打印单词出现的次数
        System.out.println(map);
    }

    /**
     * Declare the output schema for all the streams of this topology.
     *
     * @param declarer this is used to declare output stream ids, output fields, and whether or not
     *                 each output stream is a direct stream
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
