package team.moca.camo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.moca.camo.common.AuthenticateArgumentResolver;
import team.moca.camo.security.jwt.JwtUtils;

import java.util.Collections;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;

    public WebMvcConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticateArgumentResolver(jwtUtils));
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return new RestTemplate(Collections.singletonList(converter));
    }
}
