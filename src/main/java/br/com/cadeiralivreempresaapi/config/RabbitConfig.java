package br.com.cadeiralivreempresaapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app-config.topic.biot-admin}")
    private String biotAdminTopic;
    @Value("${app-config.queue.usuario-cadeira-livre-empresa-cadastro}")
    private String usuarioCadeiraLivreEmpresaCadastroMq;
    @Value("${app-config.key.usuario-cadeira-livre-empresa-cadastro}")
    private String usuarioCadeiraLivreEmpresaCadastroKey;
    @Value("${app-config.queue.enviar-notificacao}")
    private String enviarNotificacaoMq;
    @Value("${app-config.key.enviar-notificacao}")
    private String enviarNotificacaoKey;

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(biotAdminTopic);
    }

    @Bean
    Queue usuarioCadeiraLivreEmpresaCadastroMq() {
        return new Queue(usuarioCadeiraLivreEmpresaCadastroMq, true);
    }

    @Bean
    public Binding usuarioCadeiraLivreEmpresaCadastroBinding(TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadeiraLivreEmpresaCadastroMq())
            .to(exchange).with(usuarioCadeiraLivreEmpresaCadastroKey);
    }

    @Bean
    Queue enviarNotificacaoMq() {
        return new Queue(enviarNotificacaoMq, false);
    }

    @Bean
    public Binding enviarNotificacaoBinding(TopicExchange exchange) {
        return BindingBuilder.bind(enviarNotificacaoMq()).to(exchange).with(enviarNotificacaoKey);
    }
}
