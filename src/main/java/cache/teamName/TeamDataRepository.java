package cache.teamName;

import cache.model.TeamData;
import service.teamData.model.TeamDataMaximum;

import java.util.List;

public interface TeamDataRepository {

    boolean invalidTeamName(String teamName);

    List<String> getTeamNames();

    TeamData getTeamData(String team);

    TeamDataMaximum getTeamDataMaximum();

}
