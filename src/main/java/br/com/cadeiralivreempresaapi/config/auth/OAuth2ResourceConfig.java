package br.com.cadeiralivreempresaapi.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import static br.com.cadeiralivreempresaapi.modulos.usuario.enums.EPermissao.PROPRIETARIO;
import static br.com.cadeiralivreempresaapi.modulos.usuario.enums.EPermissao.ADMIN;
import static br.com.cadeiralivreempresaapi.modulos.usuario.enums.EPermissao.SOCIO;
import static java.util.Arrays.asList;

@Configuration
@EnableResourceServer
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

    private static final String TEST_PROFILE = "test";

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private Environment environment;

    @Override
    @SuppressWarnings({"checkstyle:methodlength"})
    public void configure(HttpSecurity http) throws Exception {
        String[] permitAll = {
            "/login/**",
            "/oauth/token",
            "/oauth/authorize",
            "/api/usuarios/proprietario",
            "/api/usuarios/is-authenticated",
            "/swagger-ui.html**",
            "/swagger-resources/**",
            "/v2/api-docs**",
            "/webjars/**",
            "/api/docs"
        };

        http
            .addFilterBefore(new CorsConfigFilter(), ChannelProcessingFilter.class)
            .requestMatchers()
            .antMatchers("/**")
            .and()
            .authorizeRequests()
            .antMatchers(permitAll).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/api/usuarios/usuario-autenticado").authenticated()
            .antMatchers("/api/usuarios/get-token").authenticated()
            .antMatchers("/api/usuarios/is-authenticated").authenticated()
            .antMatchers("/api/usuarios/atualizar-token-notificacao").authenticated()
            .antMatchers("/api/notificacoes/usuario/**").hasRole(ADMIN.name())
            .antMatchers("/api/usuarios/**").hasAnyRole(ADMIN.name(), PROPRIETARIO.name(), SOCIO.name())
            .antMatchers("/api/empresas/**")
            .hasAnyRole(ADMIN.name(), PROPRIETARIO.name(), SOCIO.name());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        if (asList(environment.getActiveProfiles()).contains(TEST_PROFILE)) {
            resources.stateless(false);
        }
        resources.tokenStore(tokenStore);
    }
}
