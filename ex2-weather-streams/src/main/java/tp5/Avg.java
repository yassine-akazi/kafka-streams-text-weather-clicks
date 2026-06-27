package ma.enset.tp5;
import java.io.Serializable;
public class Avg implements Serializable {
    public String station=""; public double sumTempF=0; public double sumHumidity=0; public long count=0; public double temperatureC=0;
    public Avg() {}
    public Avg(String station, double temperatureC, double humidity){ this.station=station; this.temperatureC=temperatureC; this.sumTempF=(temperatureC*9/5)+32; this.sumHumidity=humidity; this.count=1; }
    public Avg add(Avg m){ this.station=m.station; this.sumTempF+=m.sumTempF; this.sumHumidity+=m.sumHumidity; this.count+=1; return this; }
    public String format(){ return String.format("Temperature moyenne = %.1f F, Humidite moyenne = %.1f %%", sumTempF/count, sumHumidity/count); }
}
