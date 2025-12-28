package testing.testcontainers.config.dev;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import({PostgresContainerBean.class, KafkaContainerBean.class})
public class TestContainersConfig {
}