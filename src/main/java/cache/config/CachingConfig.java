package cache.config;

import cache.teamName.TeamNameRepository;
import cache.teamName.TeamNameRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingConfig {

    @Bean
    public TeamNameRepository teamNameRepository() {
        return new TeamNameRepositoryImpl();
    }

}
