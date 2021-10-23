package cache.teamName;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class is responsible for loading data from Team-Data table, as well
 * as control of accessing team specific data.
 */
@Slf4j
public class TeamNameRepositoryImpl implements TeamNameRepository {

    private static final String TABLE_NAME = "Team-Data";
    private static final String TABLE_ATTRIBUTE_NAME = "Team";
    private static Set<String> TEAM_NAMES = new HashSet();

    @Autowired DynamoDB dynamoDB;

    @PostConstruct
    public void init() {
        try {
            log.info("Loading registered team names from Team-Data table...");

            Table table = dynamoDB.getTable(TABLE_NAME);

            ScanSpec scanSpec = new ScanSpec().withProjectionExpression(TABLE_ATTRIBUTE_NAME);

            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

            Iterator<Item> iter = items.iterator();


            while (iter.hasNext()) {
                Item item = iter.next();
                TEAM_NAMES.add((String) item.get(TABLE_ATTRIBUTE_NAME));
            }

            log.info("Successfully loaded registered team names from Team-Data table.");
            log.info("Team name set contains {} items.", TEAM_NAMES.size());
        } catch (Exception e) {
            log.error("Exception occurred whilst retrieving team name from Team-Data table {}", e.getMessage());
        }
    }

    @Override
    public boolean invalidTeamName(String teamName) {
        return !TEAM_NAMES.contains(teamName);
    }

}
