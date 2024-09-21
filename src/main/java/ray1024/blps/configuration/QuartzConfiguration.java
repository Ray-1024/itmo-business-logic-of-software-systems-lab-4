package ray1024.blps.configuration;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ray1024.blps.schedule.NotificationJob;

import java.util.UUID;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(NotificationJob.class)
                .storeDurably()
                .withIdentity(UUID.randomUUID().toString())
                .withDescription("Send notification to Kafka")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(UUID.randomUUID().toString())
                .withDescription("triger")
                .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(5))
                .build();
    }
}
