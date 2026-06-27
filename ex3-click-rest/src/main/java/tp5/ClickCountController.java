package ma.enset.tp5;
import org.springframework.kafka.annotation.KafkaListener; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.concurrent.ConcurrentHashMap;
@RestController public class ClickCountController {
 private final Map<String,Long> counts=new ConcurrentHashMap<>();
 @KafkaListener(topics="click-counts", groupId="click-rest-api") public void listen(org.apache.kafka.clients.consumer.ConsumerRecord<String,String> record){ try{ counts.put(record.key(), Long.parseLong(record.value())); }catch(Exception ignored){} }
 @GetMapping("/clicks/count") public Map<String,Object> count(){ long total=counts.values().stream().mapToLong(Long::longValue).sum(); Map<String,Object> res=new LinkedHashMap<>(); res.put("totalClicks", total); res.put("byUser", new TreeMap<>(counts)); return res; }
}
