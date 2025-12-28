package testing.testcontainers.config.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;
import testing.AppRunner;

@Profile("dev")
public class AppRunnerWithTestContainers {
	static void main(String[] args) {
		SpringApplication.from(AppRunner::main).with(TestContainersConfig.class).run(args);
	}
}