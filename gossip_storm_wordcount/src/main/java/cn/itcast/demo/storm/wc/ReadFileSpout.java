package cn.itcast.demo.storm.wc;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: ReadFileSpout @ProjectName gossip_spider_parent
 * @date: 2019/2/13 16:54
 * @description: 读取text.txt数据, 向下游bolt发送句子
 */
public class ReadFileSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private BufferedReader reader;

    /**
     * 类似于构造方法 在类创建的时候会执行一次 获取数据
     *
     * @param map                  storm集群和用户自定义的配置 一般不用
     * @param topologyContext      上下文对象 一般不用
     * @param spoutOutputCollector 可以将数据读取到collector 由collector将数据发送给storm框架
     */
    @Override
    public void open(
            Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        try {

            String root = System.getProperty("user.dir");
            String filePath = root + File.separator + "text.txt";
            reader = new BufferedReader(new FileReader("gossip_storm_wordcount/src/text.txt"));

            this.collector = spoutOutputCollector;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不断的读取数据,向下游发送数据
     */
    @Override
    public void nextTuple() {
        try {
            // 读取一行句子
            String lineWord = reader.readLine();
            // 发送数据到下游bolt
            // Values 本质就是ArrayList
            collector.emit(new Values(lineWord));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义输出字段
     *
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("juzi"));
    }
}
