package controller;

import cache.teamName.TeamDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.exception.InternalServiceException;
import service.exception.InvalidServiceRequestException;
import service.exception.ServiceException;
import service.prediction.PredictionServiceCaller;
import service.prediction.model.request.PredictionRequest;
import service.prediction.model.response.PredictionResponse;
import service.teamData.TeamDataCaller;
import service.teamData.model.MatchDayTeamData;
import service.teamData.model.TeamDataMaximum;
import service.teamData.model.TeamNameResponse;

@Slf4j
@RestController
public class ServiceController {

    final TeamDataRepository teamDataRepository;
    final PredictionServiceCaller predictionServiceCaller;
    final TeamDataCaller teamDataCaller;

    public ServiceController(TeamDataRepository teamDataRepository, PredictionServiceCaller predictionServiceCaller, TeamDataCaller teamDataCaller) {
        this.teamDataRepository = teamDataRepository;
        this.predictionServiceCaller = predictionServiceCaller;
        this.teamDataCaller = teamDataCaller;
    }

    @PostMapping(value = "/predict", produces = "application/json")
    public ResponseEntity<PredictionResponse> predict(@RequestBody PredictionRequest predictionRequest) throws InvalidServiceRequestException, InternalServiceException {
        invalidTeamNameCheck(predictionRequest.getLeagueInfo().getHomeTeam(),
                predictionRequest.getLeagueInfo().getAwayTeam());

        PredictionResponse predictionResponse = predictionServiceCaller.predictMatch(predictionRequest);

        return ResponseEntity.ok(predictionResponse);
    }

    @GetMapping(value = "/teamName", produces = "application/json")
    public ResponseEntity<TeamNameResponse> getTeamNames() {
        TeamNameResponse teamNameResponse = teamDataCaller.getTeamNames();
        return ResponseEntity.ok(teamNameResponse);
    }

    @PostMapping(value = "/teamData/match", produces = "application/json")
    public ResponseEntity<MatchDayTeamData> getTeamData(@RequestBody PredictionRequest predictionRequest) throws InvalidServiceRequestException {
        String homeTeam = predictionRequest.getLeagueInfo().getHomeTeam();
        String awayTeam = predictionRequest.getLeagueInfo().getAwayTeam();

        invalidTeamNameCheck(homeTeam, awayTeam);

        MatchDayTeamData matchDayTeamData = teamDataCaller.getTeamData(homeTeam, awayTeam);

        return ResponseEntity.ok(matchDayTeamData);
    }

    @GetMapping(value = "/teamData/max", produces = "application/json")
    public ResponseEntity<TeamDataMaximum> getTeamDataMaximum() {
        TeamDataMaximum teamDataMaximum = teamDataCaller.getTeamDataMaximum();
        return ResponseEntity.ok(teamDataMaximum);
    }

    @ExceptionHandler({InvalidServiceRequestException.class})
    public ResponseEntity<ServiceException> handleBadRequestException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ExceptionHandler({InternalServiceException.class})
    public ResponseEntity<ServiceException> handleInternalServiceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }

    private void invalidTeamNameCheck(String homeTeam, String awayTeam) throws InvalidServiceRequestException {
        if (teamDataRepository.invalidTeamName(homeTeam) || teamDataRepository.invalidTeamName(awayTeam)) {
            log.warn("Team name are not registered in cache. Try Again.");
            throw new InvalidServiceRequestException("Home and/or away team is not recognized.");
        }
    }
}
