package common;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class DynamoClient {

    private final DynamoDB dynamoDB;

    DynamoClient(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public Iterator<Item> getTableItems(String tableName, String tableAttributes) {
        Table table = dynamoDB.getTable(tableName);

        ScanSpec scanSpec = new ScanSpec().withProjectionExpression(tableAttributes);

        ItemCollection<ScanOutcome> items = table.scan(scanSpec);

        return items.iterator();
    }

    public Iterator<Item> getTableItems(String tableName, String keyCondition, ValueMap valueMap) {
        Table table = dynamoDB.getTable(tableName);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression(keyCondition)
                .withValueMap(valueMap);

        ItemCollection<QueryOutcome> items = table.query(querySpec);

        return items.iterator();
    }
}
