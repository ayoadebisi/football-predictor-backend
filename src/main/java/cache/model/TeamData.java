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
public class TeamData {

    @JsonProperty("Team")
    String team;

    @JsonProperty("UnbeatenStreak")
    Integer unbeatenStreak;

    @JsonProperty("CleanSheet")
    Integer cleanSheet;

    @JsonProperty("Form")
    Integer form;

    @JsonProperty("PerformanceRating")
    Integer performanceRating;

    @JsonProperty("OffensiveRating")
    Integer offensiveRating;

    @JsonProperty("DefensiveRating")
    Integer defensiveRating;

}
