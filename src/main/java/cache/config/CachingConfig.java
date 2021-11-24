package cache.config;

import cache.teamName.TeamNameRepository;
import cache.teamName.TeamNameRepositoryImpl;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CachingConfig {

    private final Environment env;

    public CachingConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public TeamNameRepository teamNameRepository(DynamoDB dynamoDB) {
        return new TeamNameRepositoryImpl(dynamoDB, env);
    }

}
