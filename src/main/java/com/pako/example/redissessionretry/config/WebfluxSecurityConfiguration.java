package com.pako.example.redissessionretry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.WebSession;

import java.net.URI;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class WebfluxSecurityConfiguration {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
			.username("user")
			.password("user")
			.roles("USER")
			.build();
		return new MapReactiveUserDetailsService(user);
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange(exchanges -> exchanges
						.anyExchange().authenticated()
				)
				.formLogin(withDefaults())
				.httpBasic(withDefaults())
				.logout().logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler());
		return http.build();
	}

	private ServerLogoutSuccessHandler logoutSuccessHandler() {
		RedirectServerLogoutSuccessHandler redirectHandler = new RedirectServerLogoutSuccessHandler();
		redirectHandler.setLogoutSuccessUrl(URI.create("/"));
		return (exchange, authentication) -> {
			return   exchange.getExchange().getSession()
					.doOnNext(session -> session.getAttributes().clear())
                    .flatMap(WebSession::invalidate)
					.then(redirectHandler.onLogoutSuccess(exchange, authentication));
        };
	}

}
