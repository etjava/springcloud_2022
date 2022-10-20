package springcloud_student_consumer_hystrix_dashborad_90;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableHystrixDashboard
public class StudentConsumerDashBoardApp_90 {
 
    public static void main(String[] args) {
        SpringApplication.run(StudentConsumerDashBoardApp_90.class, args);
    }
}
