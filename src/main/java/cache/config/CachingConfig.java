package cache.config;

import cache.teamName.TeamDataRepository;
import cache.teamName.TeamDataRepositoryImpl;
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
    public TeamDataRepository teamDataRepository(DynamoDB dynamoDB) {
        return new TeamDataRepositoryImpl(dynamoDB, env);
    }

}
