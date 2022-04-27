package com.example.graphql.autoconfig;


import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Turns off authorization for everything <em>unless</em> somebody specifies the property to enable it. Makes
 * it easier to introduce the feature as we need it, but have it backoff for earlier work.
 *
 * @author Josh Long
 */

@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
class GraphqlSecurityAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "bootiful.graphql.security", name = "enabled", havingValue = "false", matchIfMissing = true)
    public static class DefaultNoopSecurityAutoConfiguration {

        @Bean
        MapReactiveUserDetailsService authentication() {
            return new MapReactiveUserDetailsService(User.withDefaultPasswordEncoder().username("user").password("pw").roles("USER").build());
        }

        @Bean
        SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
            return http
                    .cors(withDefaults())
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(requests -> requests.anyExchange().permitAll())
                    .httpBasic(withDefaults())
                    .build();
        }
    }
}
