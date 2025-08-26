// package com.afr.fms.Security.WebSocket;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// import com.afr.fms.Payload.endpoint.Endpoint;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry config) {
//         config.enableSimpleBroker("/api");
//         config.setApplicationDestinationPrefixes("/app");
//     }

//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {

//         registry.addEndpoint("/api").setAllowedOrigins(Endpoint.URL).withSockJS();
//     }
// }