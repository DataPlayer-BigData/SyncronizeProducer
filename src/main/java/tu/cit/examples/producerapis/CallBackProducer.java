package tu.cit.examples.producerapis;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CallBackProducer {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args)  {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"my-sync-producer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        props.put(ProducerConfig.RETRIES_CONFIG,0);
        props.put(ProducerConfig.ACKS_CONFIG,"all");

//        InputStream input = new FileInputStream("kafka.properties");
//        if(input == null ){
//            logger.error("kafka.properties file is empty or corrupted. Please check kafka.properties file.");
//        }else{
//            props.load(input);
//         }

        //METHOD 1
//        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);
//        for(int i=10;i<=13;i++)
//        {
//            String msg = "Call Back Producer testing " + i;
//            producer.send(new ProducerRecord<String, String>("sync-prdoucer", "KEY3", msg),new Callback(){
//                public void onCompletion(RecordMetadata metadata, Exception e) {
//                    if(e != null) {
//                        e.printStackTrace();
//                    } else {
//                        logger.info("Message at offset : " + metadata.offset());
//                    }
//                }
//            });
//        }
//        producer.close();

        //METHOD 2
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);
        for(int i=45;i<=46;i++)
        {
            String msg = "Call Back Producer testing " + i;
            producer.send(new ProducerRecord<String, String>("sync-prdoucer", "KEY3", msg),new MyCallBack(msg));
            //System.out.println("Producer metrics : " + producer.metrics());
        }
        //System.out.println("Partition Details of topic : sync-prdoucer - "+ producer.partitionsFor("sync-producer"));
        logger.info("Finished Applicatoin -  Closing Producer.");
        producer.close();


    }
}

class MyCallBack implements Callback{
    //private static final Logger logger = LogManager.getLogger();
    private String msg;

    public MyCallBack(String msg){
        this.msg =msg;
    }


    public void onCompletion(RecordMetadata metadata, Exception e){
        if(e != null){
            //logger.error("Error in sending message =" + msg);
            System.out.println("Error in sending message =" + msg);
        }else{
            //logger.info(msg + " has been sent at offset : "+metadata.offset());
            System.out.println(msg + " has been sent at offset : "+metadata.offset());
        }

    }
}