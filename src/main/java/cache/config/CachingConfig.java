package cache.config;

import cache.headToHead.HeadToHeadRepository;
import cache.headToHead.HeadToHeadRepositoryImpl;
import cache.teamName.TeamDataRepository;
import cache.teamName.TeamDataRepositoryImpl;
import common.DynamoClient;
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
    public TeamDataRepository teamDataRepository(DynamoClient dynamoClient) {
        return new TeamDataRepositoryImpl(dynamoClient, env);
    }

    @Bean
    public HeadToHeadRepository headToHeadRepository(DynamoClient dynamoClient) {
        return new HeadToHeadRepositoryImpl(dynamoClient, env);
    }

}
