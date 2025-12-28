package testing.testcontainers.config.dev;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class KafkaContainerBean {

    /*
    @Bean
    @ServiceConnection
    public KafkaContainer kafka() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
                .withKraft()
                .withReuse(true); // Solo un contenedor compartido con n microservicios
    }
    */
}
