package ma.enset.tp5;
import com.fasterxml.jackson.databind.ObjectMapper; import org.apache.kafka.common.serialization.*;
public class AvgSerde implements Serde<Avg> { private final ObjectMapper mapper=new ObjectMapper();
 public Serializer<Avg> serializer(){ return (topic,data)->{ try{return mapper.writeValueAsBytes(data);}catch(Exception e){throw new RuntimeException(e);} }; }
 public Deserializer<Avg> deserializer(){ return (topic,data)->{ try{return data==null?null:mapper.readValue(data,Avg.class);}catch(Exception e){throw new RuntimeException(e);} }; }
}
