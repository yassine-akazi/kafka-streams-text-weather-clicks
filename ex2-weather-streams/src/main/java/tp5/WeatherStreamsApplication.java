package ma.enset.tp5;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;

@SpringBootApplication
public class WeatherStreamsApplication implements CommandLineRunner {
    public static void main(String[] args) { SpringApplication.run(WeatherStreamsApplication.class, args); }
    @Override public void run(String... args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "weather-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("weather-data");
        KTable<String, Avg> averages = source
                .mapValues(WeatherStreamsApplication::parse)
                .filter((k, v) -> v != null && v.temperatureC > 30)
                .selectKey((k, v) -> v.station)
                .groupByKey(Grouped.with(Serdes.String(), new AvgSerde()))
                .aggregate(Avg::new, (station, measure, avg) -> avg.add(measure), Materialized.with(Serdes.String(), new AvgSerde()));
        averages.toStream().mapValues(Avg::format).to("station-averages", Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); streams.start();
    }
    static Avg parse(String line) {
        try { String[] p=line.split(","); if(p.length!=3) return null; return new Avg(p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim())); }
        catch(Exception e){ return null; }
    }
}
