package cache.teamName;

import cache.model.TeamData;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.Constants.PROD;
import static common.Constants.STAGE_KEY;
import static common.RandomNumberGenerator.generateRandomInt;

/**
 * Class is responsible for loading data from Team-Data table, as well
 * as control of accessing team specific data.
 */
@Slf4j
@Component
public class TeamDataRepositoryImpl implements TeamDataRepository {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String TABLE_NAME = "Team-Data";
    private static final String TABLE_ATTRIBUTE_NAME = "Team, Form, CleanSheet, UnbeatenStreak, " +
            "PerformanceRating, OffensiveRating, DefensiveRating";
    private static final int UPPER_BOUND_RATING = 100;
    private static final int UPPER_BOUND_STREAK = 60;
    private static final int UPPER_BOUND_FORM = 15;
    private static Map<String, TeamData> TEAM_DATA = new HashMap();

    private final DynamoDB dynamoDB;
    private final Environment env;

    public TeamDataRepositoryImpl(DynamoDB dynamoDB, Environment env) {
        this.dynamoDB = dynamoDB;
        this.env = env;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Loading registered team names from Team-Data table...");

            String stage = env.getProperty(STAGE_KEY);

            if (!PROD.equalsIgnoreCase(stage)) {
                log.info("Retrieving team name for Devo stage.");
                TEAM_DATA.put("Chelsea", generateMockData("Chelsea"));
                TEAM_DATA.put("Liverpool", generateMockData("Liverpool"));
                TEAM_DATA.put("Manchester United", generateMockData("Manchester United"));
                TEAM_DATA.put("Manchester City", generateMockData("Manchester City"));
                TEAM_DATA.put("West Ham", generateMockData("West Ham"));
                TEAM_DATA.put("Arsenal", generateMockData("Arsenal"));
            } else {
                log.info("Retrieving team name for Prod stage.");

                Table table = dynamoDB.getTable(TABLE_NAME);

                ScanSpec scanSpec = new ScanSpec().withProjectionExpression(TABLE_ATTRIBUTE_NAME);

                ItemCollection<ScanOutcome> items = table.scan(scanSpec);

                Iterator<Item> iter = items.iterator();

                while (iter.hasNext()) {
                    Item item = iter.next();
                    TeamData teamData = convertItemToTeamData(item.asMap());
                    TEAM_DATA.put(teamData.getTeam(), teamData);
                }
            }

            log.info("Successfully loaded registered team names from Team-Data table.");
            log.info("Team name set contains {} items.", TEAM_DATA.size());
        } catch (Exception e) {
            log.error("Exception occurred whilst retrieving team name from Team-Data table {}", e.getMessage());
        }
    }

    @Override
    public boolean invalidTeamName(String teamName) {
        return !TEAM_DATA.containsKey(teamName);
    }

    @Override
    public List<String> getTeamNames() {
        return TEAM_DATA.keySet().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public TeamData getTeamData(String team) {
        return TEAM_DATA.get(team);
    }

    private TeamData convertItemToTeamData(Map<String, Object> item) {
        return MAPPER.convertValue(item, TeamData.class);
    }

    private TeamData generateMockData(String teamName) {
        return TeamData.builder()
                .team(teamName)
                .cleanSheet(generateRandomInt(UPPER_BOUND_STREAK))
                .form(generateRandomInt(UPPER_BOUND_FORM))
                .unbeatenStreak(generateRandomInt(UPPER_BOUND_STREAK))
                .performanceRating(generateRandomInt(UPPER_BOUND_RATING))
                .offensiveRating(generateRandomInt(UPPER_BOUND_RATING))
                .defensiveRating(generateRandomInt(UPPER_BOUND_RATING))
                .build();
    }

}
