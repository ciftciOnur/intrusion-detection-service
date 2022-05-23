package tr.edu.yeditepe.intrusiondetection;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntrusionDetectionApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(IntrusionDetectionApplication.class, args);
    }

    @Bean
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Istanbul")));
    }
}
