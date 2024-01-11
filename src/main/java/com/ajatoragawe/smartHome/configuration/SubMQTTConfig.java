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

import java.util.Objects;

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
                    String topic = Objects.requireNonNull(headers.get("mqtt_receivedTopic")).toString();
                    String[] parts = payload.split(";");

                    // Check if the payload has both timestamp and value
                    if (parts.length == 2) {
                        String timestamp = parts[0];
                        String value = parts[1];

                        // Create JSON object
                        String jsonPayload = String.format("{\"topic\":\"%s\",\"timestamp\":\"%s\",\"value\":\"%s\"}", topic, timestamp, value);

                        // Send JSON payload to WebSocket
                        messagingTemplate.convertAndSend("/topic/messages", jsonPayload);
                    } else {
                        System.err.println("Invalid payload format: " + payload);
                    }
                    return null;
                })
                .get();
    }


    @Bean
    MqttPahoMessageDrivenChannelAdapter inboundAdapter(MqttPahoClientFactory clientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("consumer", clientFactory, topics);
        return adapter;
    }
}

