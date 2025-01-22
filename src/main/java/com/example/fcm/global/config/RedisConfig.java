package com.example.fcm.global.config;

import com.example.fcm.infra.redis.PushMessageSubscriber;
import com.example.fcm.infra.redis.RetryPushMessageSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    //Lettuce 사용
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        // ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // GenericJackson2JsonRedisSerializer 사용 (새로운 방식)
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setValueSerializer(jsonSerializer);
        return template;
    }

    // 일반 메시지용 토픽
    @Bean
    public ChannelTopic normalChannelTopic() {
        return new ChannelTopic("push-queue:normal");
    }

    // 재시도 메시지용 토픽
    @Bean
    public ChannelTopic retryChannelTopic() {
        return new ChannelTopic("push-queue:retry");
    }

    // 일반 메시지 리스너
    @Bean
    public MessageListenerAdapter normalListenerAdapter(PushMessageSubscriber subscriber) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber);
        adapter.setDefaultListenerMethod("handleMessage");
        return adapter;
    }

    // 재시도 메시지 리스너
    @Bean
    public MessageListenerAdapter retryListenerAdapter(RetryPushMessageSubscriber subscriber) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber);
        adapter.setDefaultListenerMethod("handleRetryMessage");
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            MessageListenerAdapter normalListenerAdapter,
            MessageListenerAdapter retryListenerAdapter,
            ChannelTopic normalChannelTopic,
            ChannelTopic retryChannelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        // 일반 메시지 리스너 등록
        container.addMessageListener(normalListenerAdapter, normalChannelTopic);
        // 재시도 메시지 리스너 등록
        container.addMessageListener(retryListenerAdapter, retryChannelTopic);

        return container;
    }
}
