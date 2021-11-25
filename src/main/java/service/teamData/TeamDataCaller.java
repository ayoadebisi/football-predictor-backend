package service.teamData;

import cache.model.TeamData;
import cache.teamName.TeamDataRepository;
import org.springframework.stereotype.Component;
import service.teamData.model.MatchDayTeamData;
import service.teamData.model.TeamDataMaximum;
import service.teamData.model.TeamDataResponse;
import service.teamData.model.TeamNameResponse;

import java.util.List;

@Component
public class TeamDataCaller {

    final TeamDataRepository teamDataRepository;

    public TeamDataCaller(TeamDataRepository teamDataRepository) {
        this.teamDataRepository = teamDataRepository;
    }

    public TeamNameResponse getTeamNames() {
        List<String> teamNames = teamDataRepository.getTeamNames();
        return TeamNameResponse.builder().teamNames(teamNames).build();
    }

    public MatchDayTeamData getTeamData(String homeTeam, String awayTeam) {
        TeamData homeTeamData = teamDataRepository.getTeamData(homeTeam);
        TeamData awayTeamData = teamDataRepository.getTeamData(awayTeam);

        return MatchDayTeamData.builder()
                .homeTeam(mapTeamDataToResponse(homeTeamData))
                .awayTeam(mapTeamDataToResponse(awayTeamData))
                .build();
    }

    public TeamDataMaximum getTeamDataMaximum() {
        return teamDataRepository.getTeamDataMaximum();
    }

    private TeamDataResponse mapTeamDataToResponse(TeamData teamData) {
        return TeamDataResponse.builder()
                .teamName(teamData.getTeam())
                .cleanSheet(teamData.getCleanSheet())
                .form(teamData.getForm())
                .unbeaten(teamData.getUnbeatenStreak())
                .overall(teamData.getPerformanceRating())
                .offense(teamData.getOffensiveRating())
                .defense(teamData.getDefensiveRating())
                .build();
    }

}
