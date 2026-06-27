package ma.enset.tp5;
import org.apache.kafka.common.serialization.Serdes; import org.apache.kafka.streams.*; import org.apache.kafka.streams.kstream.*; import org.springframework.boot.*; import org.springframework.boot.autoconfigure.SpringBootApplication; import java.util.Properties;
@SpringBootApplication public class ClickCounterApplication implements CommandLineRunner{
 public static void main(String[] args){SpringApplication.run(ClickCounterApplication.class,args);} public void run(String... args){
  Properties props=new Properties(); props.put(StreamsConfig.APPLICATION_ID_CONFIG,"click-counter-app"); props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass()); props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
  StreamsBuilder builder=new StreamsBuilder(); KStream<String,String> clicks=builder.stream("clicks");
  clicks.filter((user,action)->"click".equalsIgnoreCase(action)).groupByKey(Grouped.with(Serdes.String(),Serdes.String())).count(Materialized.as("clicks-by-user-store")).toStream().mapValues(Object::toString).to("click-counts", Produced.with(Serdes.String(),Serdes.String()));
  KafkaStreams streams=new KafkaStreams(builder.build(),props); Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); streams.start(); }}
