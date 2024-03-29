package webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static common.Constants.DEVO;
import static common.Constants.PROD;
import static common.Constants.STAGE_KEY;

@Configuration
@PropertySource("classpath:application.properties")
public class WebClientConfig {

    private static final Map<String, String> ENDPOINT_MAP =
            Map.of(DEVO, "http://0.0.0.0:5000/v1/ModelDeveloperService",
                    PROD, "https://model-developer-service.herokuapp.com/v1/ModelDeveloperService");

    private final Environment env;

    public WebClientConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public WebClient webClient() {
        String stage = env.getProperty(STAGE_KEY);
        return WebClient.builder()
                .baseUrl(ENDPOINT_MAP.get(stage))
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
