package depavlo.millionredroses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EntityScan({ "depavlo.millionredroses.model" })
@EnableJpaRepositories({ "depavlo.millionredroses.repo" })
@ComponentScan({ "depavlo.millionredroses" })
public class MillionRedRosesApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MillionRedRosesApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MillionRedRosesApplication.class, args);
	}

}
