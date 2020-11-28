package tu.cit.examples.producerapis;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Properties;

public class SyncronizeProducer {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"my-sync-producer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        props.put(ProducerConfig.RETRIES_CONFIG,0);
        props.put(ProducerConfig.ACKS_CONFIG,"all"); // This is just to test CASE 2. It will raise an exception NotEnoughReplicasException
        //props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy"); //Default value is none. gzip, snappy, lz4, or zstd are possible values.


        //If you want to explore more about KAFKA PRODUCER, visit below links
        //https://kafka.apache.org/24/documentation.html#brokerconfigs
        //https://kafka.apache.org/24/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
        //https://downloads.apache.org/kafka/2.4.0/javadoc/org/apache/kafka/clients/producer/ProducerConfig.html

        RecordMetadata metadata;
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(props);

        try{
            for(int i=1;i<=3;i++){
                //Thread.sleep(7000); // This is just to test CASE 2
                ProducerRecord<String,String> record = new ProducerRecord("sync-producer","KEY"+i, "Simple Message : "+i);
                metadata=producer.send(record).get();

                logger.info("Topic Name : " +metadata.topic() + ", Partition : " + metadata.partition() + ", Offset : " + metadata.offset() +
                        ", TimeStamp : " + new Timestamp(metadata.timestamp()) + ", key : " + record.key() + " , Value : " + record.value());
            }

        }catch(Exception e){
            logger.info("Can't send message - Received exception \n" + e.getMessage());
           // e.printStackTrace();
        }
    }
}
