package ma.enset.tp5;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class TextCleanerApplication implements CommandLineRunner {
    private static final List<String> FORBIDDEN = List.of("HACK", "SPAM", "XXX");
    public static void main(String[] args) { SpringApplication.run(TextCleanerApplication.class, args); }

    @Override public void run(String... args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "text-cleaner-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> input = builder.stream("text-input");
        KStream<String, String> cleaned = input.mapValues(v -> v == null ? "" : v.trim().replaceAll("\\s+", " ").toUpperCase());
        cleaned.filter((k, v) -> isValid(v)).to("text-clean");
        cleaned.filter((k, v) -> !isValid(v)).mapValues(v -> "Message rejeté : " + v).to("text-dead-letter");
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
    private static boolean isValid(String msg) {
        return msg != null && !msg.isBlank() && msg.length() <= 100 && FORBIDDEN.stream().noneMatch(msg::contains);
    }
}
