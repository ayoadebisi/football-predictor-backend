package service.prediction.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueInfo {

    private String country;

    @NonNull
    @JsonProperty("home_team")
    private String homeTeam;

    @NonNull
    @JsonProperty("away_team")
    private String awayTeam;

}
