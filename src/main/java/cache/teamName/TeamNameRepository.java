package cache.teamName;

import java.util.List;

public interface TeamNameRepository {

    boolean invalidTeamName(String teamName);

    List<String> getTeamNames();

}
