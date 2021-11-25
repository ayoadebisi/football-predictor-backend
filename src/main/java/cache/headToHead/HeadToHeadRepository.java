package cache.headToHead;

import cache.model.HeadToHeadData;

public interface HeadToHeadRepository {

    HeadToHeadData getHeadToHeadData(String homeTeam, String awayTeam);

}
