package com.ajatoragawe.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

@Configuration
public class SubMQTTConfig {
    //Cuman dipake test, ga dipakai sebenarnya.
    @Bean
    public IntegrationFlow inboundFlow(MqttPahoMessageDrivenChannelAdapter inboundAdapter) {
        return IntegrationFlow
                .from(inboundAdapter)
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println("New message!"+payload);
                    headers.forEach((k, v) -> System.out.println(k + "=" + v));
                    return null;
                })
                .get();
    }
    @Bean
    MqttPahoMessageDrivenChannelAdapter inboundAdapter(@Value("${hivemq.topic}") String topic,
                                                       MqttPahoClientFactory clientFactory){
        return new MqttPahoMessageDrivenChannelAdapter("consumer", clientFactory, topic);
    }
}