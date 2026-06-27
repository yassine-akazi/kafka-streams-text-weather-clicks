package ma.enset.tp5;
import org.springframework.kafka.core.KafkaTemplate; import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.*;
@Controller
public class ClickController {
 private final KafkaTemplate<String,String> kafkaTemplate; public ClickController(KafkaTemplate<String,String> kafkaTemplate){this.kafkaTemplate=kafkaTemplate;}
 @GetMapping("/") public String index(){return "index";}
 @PostMapping("/click") @ResponseBody public String click(@RequestParam(defaultValue="user1") String userId){ kafkaTemplate.send("clicks", userId, "click"); return "OK"; }
}
