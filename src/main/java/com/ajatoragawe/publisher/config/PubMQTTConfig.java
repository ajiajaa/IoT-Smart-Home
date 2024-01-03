package com.ajatoragawe.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.util.ArrayList;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PubMQTTConfig {
    @Bean
    ApplicationRunner triggerMqttMessage(MqttPahoMessageHandler outboundAdapter) {
        return args -> {
            try {
                sendMqttMessage(outboundAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void sendMqttMessage(MqttPahoMessageHandler outboundAdapter) throws Exception {
//        System.out.println("List COM ports");
//        SerialPort comPorts[] = SerialPort.getCommPorts();
//        for (int i = 0; i < comPorts.length; i++)
//            System.out.println("comPorts[" + i + "] = " + comPorts[i].getDescriptivePortName());
//        int port = 2;     // array index to select COM port
//        comPorts[port].openPort();
//        System.out.println("open port comPorts[" + port + "]  " + comPorts[port].getDescriptivePortName());
//        comPorts[port].setBaudRate(9600);
//        try {
//            while (true)
//            {
//                // read serial port  and display data
//                while (comPorts[port].bytesAvailable() > 0)
//                {
//                    byte[] readBuffer = new byte[comPorts[port].bytesAvailable()];
//                    int numRead = comPorts[port].readBytes(readBuffer, readBuffer.length);
//                    //System.out.print("Read " + numRead + " bytes from COM port: ");
//                    for (int i = 0; i < readBuffer.length; i++) {
//                        System.out.print((char) readBuffer[i]);
//                        var message = MessageBuilder.withPayload(readBuffer[i]).build();
//                        outboundAdapter.handleMessage(message);
//                    }
//                    //System.out.println();
//                }
//            }
//        } catch (Exception e) { e.printStackTrace(); }
//        comPorts[port].closePort();
        ArrayList<String> arr= new ArrayList<>();
        arr.add("halo");
        arr.add("kamu");
        arr.add("yang");
        arr.add("di");
        arr.add("sana");
        for (String a: arr){
            var message  = MessageBuilder.withPayload(a.toString()).build();
            outboundAdapter.handleMessage(message);
        }

    }

    @Bean
    MqttPahoMessageHandler outboundAdapter(
            @Value("${hivemq.topic}") String topic,
            MqttPahoClientFactory factory){
        var mh = new MqttPahoMessageHandler("producer", factory);
        mh.setDefaultTopic(topic);
        return mh;
    }

    @Bean
    IntegrationFlow outboundFlow(MessageChannel out,
                                 MqttPahoMessageHandler outboundAdapter){
        return IntegrationFlow
                .from(out)
                .handle(outboundAdapter)
                .get();
    }

    @Bean
    MessageChannel out(){
        return MessageChannels.direct().getObject();
    }
}
