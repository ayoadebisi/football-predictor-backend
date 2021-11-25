package cache.headToHead;

import cache.model.HeadToHeadData;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import common.DynamoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static common.Constants.PROD;
import static common.Constants.STAGE_KEY;
import static common.DynamoObjectMapper.mapDynamoItem;
import static common.RandomNumberGenerator.generateRandomInt;

@Slf4j
@Component
public class HeadToHeadRepositoryImpl implements HeadToHeadRepository {

    private static final int UPPER_BOUND = 20;
    private static final String TABLE_NAME = "Head-To-Head-Data";
    private static final String KEY_CONDITION = "MatchId = :id";

    private final DynamoClient dynamoClient;
    private final Environment env;

    public HeadToHeadRepositoryImpl(DynamoClient dynamoClient, Environment env) {
        this.dynamoClient = dynamoClient;
        this.env = env;
    }

    @Override
    @Cacheable("headToHeadCache")
    public HeadToHeadData getHeadToHeadData(String homeTeam, String awayTeam) {
        log.info("Retrieving head to head data between {} and {}", homeTeam, awayTeam);
        return retrieveDataFromTable(homeTeam, awayTeam);
    }

    private String getHeadToHeadKey(String homeTeam, String awayTeam) {
        if (homeTeam.compareTo(awayTeam) <= 0) {
            return parseKey(homeTeam, awayTeam);
        } else {
            return parseKey(awayTeam, homeTeam);
        }
    }

    private String parseKey(String firstTeam, String secondTeam) {
        return String.format("%s-%s",
                firstTeam.replaceAll(" ", ""), secondTeam.replaceAll(" ", ""));
    }

    private HeadToHeadData retrieveDataFromTable(String homeTeam, String awayTeam) {
        HeadToHeadData headToHeadData = null;

        String stage = env.getProperty(STAGE_KEY);

        if (!PROD.equalsIgnoreCase(stage)) {
            log.info("Retrieving mock data for non-prod stage.");

            headToHeadData = generateMockData(homeTeam, awayTeam);
        } else {
            log.info("Retrieving data for prod stage.");

            String key = getHeadToHeadKey(homeTeam, awayTeam);

            ValueMap valueMap = new ValueMap().withString(":id", key);

            Iterator<Item> iter = dynamoClient.getTableItems(TABLE_NAME, KEY_CONDITION, valueMap);

            while (iter.hasNext()) {
                Item item = iter.next();
                headToHeadData = mapDynamoItem(item.asMap(), HeadToHeadData.class);
            }
        }

        headToHeadData.setFirstTeam(homeTeam.compareTo(awayTeam) <= 0 ? homeTeam : awayTeam);
        headToHeadData.setSecondTeam(homeTeam.compareTo(awayTeam) > 0 ? homeTeam : awayTeam);

        return headToHeadData;
    }

    private HeadToHeadData generateMockData(String homeTeam, String awayTeam) {
        return HeadToHeadData.builder()
                .firstTeam(homeTeam.compareTo(awayTeam) <= 0 ? homeTeam : awayTeam)
                .secondTeam(homeTeam.compareTo(awayTeam) > 0 ? homeTeam : awayTeam)
                .cleanSheetFirstTeam(generateRandomInt(UPPER_BOUND))
                .cleanSheetSecondTeam(generateRandomInt(UPPER_BOUND))
                .formFirstTeam(generateRandomInt(UPPER_BOUND))
                .formSecondTeam(generateRandomInt(UPPER_BOUND))
                .goalFirstTeam(generateRandomInt(UPPER_BOUND))
                .goalSecondTeam(generateRandomInt(UPPER_BOUND))
                .scoringFirstTeam(generateRandomInt(UPPER_BOUND))
                .scoringSecondTeam(generateRandomInt(UPPER_BOUND))
                .unbeatenFirstTeam(generateRandomInt(UPPER_BOUND))
                .unbeatenSecondTeam(generateRandomInt(UPPER_BOUND))
                .build();
    }

}
