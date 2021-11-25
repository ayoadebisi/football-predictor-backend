package cache.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeadToHeadData {

    String firstTeam;

    String secondTeam;

    @JsonProperty("FormHome")
    Integer formFirstTeam;

    @JsonProperty("FormAway")
    Integer formSecondTeam;

    @JsonProperty("GoalHome")
    Integer goalFirstTeam;

    @JsonProperty("GoalAway")
    Integer goalSecondTeam;

    @JsonProperty("CleanSheetHome")
    Integer cleanSheetFirstTeam;

    @JsonProperty("CleanSheetAway")
    Integer cleanSheetSecondTeam;

    @JsonProperty("UnbeatenHome")
    Integer unbeatenFirstTeam;

    @JsonProperty("UnbeatenAway")
    Integer unbeatenSecondTeam;

    @JsonProperty("ScoringHome")
    Integer scoringFirstTeam;

    @JsonProperty("ScoringAway")
    Integer scoringSecondTeam;

}
