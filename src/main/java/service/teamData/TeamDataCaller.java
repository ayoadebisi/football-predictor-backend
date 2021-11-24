package service.teamData;

import cache.teamName.TeamNameRepository;
import org.springframework.stereotype.Component;
import service.teamData.model.TeamDataResponse;

import java.util.List;

@Component
public class TeamDataCaller {

    final TeamNameRepository teamNameRepository;

    public TeamDataCaller(TeamNameRepository teamNameRepository) {
        this.teamNameRepository = teamNameRepository;
    }

    public TeamDataResponse getTeamData() {
        List<String> teamNames = teamNameRepository.getTeamNames();
        return TeamDataResponse.builder().teamNames(teamNames).build();
    }

}
