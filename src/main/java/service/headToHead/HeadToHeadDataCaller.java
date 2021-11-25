package service.headToHead;

import cache.headToHead.HeadToHeadRepository;
import cache.model.HeadToHeadData;
import org.springframework.stereotype.Component;
import service.headToHead.model.HeadToHeadDataResponse;

@Component
public class HeadToHeadDataCaller {

    final HeadToHeadRepository headToHeadRepository;

    public HeadToHeadDataCaller(HeadToHeadRepository headToHeadRepository) {
        this.headToHeadRepository = headToHeadRepository;
    }

    public HeadToHeadDataResponse getHeadToHeadData(String homeTeam, String awayTeam) {
        HeadToHeadData headToHeadData = headToHeadRepository.getHeadToHeadData(homeTeam, awayTeam);

        return mapToHeadToHeadResponse(headToHeadData, homeTeam);
    }

    private HeadToHeadDataResponse mapToHeadToHeadResponse(HeadToHeadData headToHeadData, String homeTeam) {
        boolean order = orderIsHomeAway(homeTeam, headToHeadData.getFirstTeam());

        int form = order
                ? headToHeadData.getFormFirstTeam() - headToHeadData.getFormSecondTeam()
                : headToHeadData.getFormSecondTeam() - headToHeadData.getFormFirstTeam();
        int cleanSheet = order
                ? headToHeadData.getCleanSheetFirstTeam() - headToHeadData.getCleanSheetSecondTeam()
                : headToHeadData.getCleanSheetSecondTeam() - headToHeadData.getCleanSheetFirstTeam();
        int goals = order
                ? headToHeadData.getGoalFirstTeam() - headToHeadData.getGoalSecondTeam()
                : headToHeadData.getGoalSecondTeam() - headToHeadData.getGoalFirstTeam();
        int unbeaten = order
                ? headToHeadData.getUnbeatenFirstTeam() - headToHeadData.getUnbeatenSecondTeam()
                : headToHeadData.getUnbeatenSecondTeam() - headToHeadData.getUnbeatenFirstTeam();
        int scored = order
                ? headToHeadData.getScoringFirstTeam() - headToHeadData.getScoringSecondTeam()
                : headToHeadData.getScoringSecondTeam() - headToHeadData.getScoringFirstTeam();

        return HeadToHeadDataResponse.builder()
                .form(form)
                .cleanSheet(cleanSheet)
                .goals(goals)
                .unbeaten(unbeaten)
                .scored(scored)
                .build();
    }

    private boolean orderIsHomeAway(String homeTeam, String firstTeam) {
        return homeTeam.equalsIgnoreCase(firstTeam);
    }
}
