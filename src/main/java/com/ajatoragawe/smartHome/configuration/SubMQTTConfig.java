package com.ajatoragawe.smartHome.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class SubMQTTConfig {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${hivemq.topics}")
    private String[] topics;

    @Bean
    public IntegrationFlow inboundFlow(MqttPahoMessageDrivenChannelAdapter inboundAdapter) {
        return IntegrationFlow
                .from(inboundAdapter)
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println("New message!" + payload);
                    headers.forEach((k, v) -> System.out.println(k + "=" + v));
                    // Send the message to the WebSocket endpoint
                    messagingTemplate.convertAndSend("/topic/messages", payload);
                    return null;
                })
                .get();
    }

    @Bean
    MqttPahoMessageDrivenChannelAdapter inboundAdapter(MqttPahoClientFactory clientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("consumer", clientFactory, topics);
        adapter.setCompletionTimeout(5000); // Set a timeout if needed
        return adapter;
    }
}

