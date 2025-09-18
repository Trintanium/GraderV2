package com.example.graderworkerapplication;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
class GraderWorkerApplicationTests {

    @Test
    void contextLoads() {
        // แค่ตรวจว่า context โหลดผ่าน
    }

    @Configuration
    static class TestConfig {

        @Bean
        public CachingConnectionFactory connectionFactory() {
            // สร้าง dummy connection factory โดยไม่ต้องเชื่อมจริง
            CachingConnectionFactory factory = new CachingConnectionFactory();
            factory.setHost("localhost"); // ไม่จำเป็นต้องมี RabbitMQ จริง
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("admin");
            return factory;
        }

        @Bean
        public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
            return new RabbitTemplate(connectionFactory);
        }
    }
}