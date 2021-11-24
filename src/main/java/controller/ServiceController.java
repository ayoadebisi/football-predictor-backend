package controller;

import cache.teamName.TeamNameRepository;
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
import service.teamData.model.TeamDataResponse;

@Slf4j
@RestController
public class ServiceController {

    final TeamNameRepository teamNameRepository;
    final PredictionServiceCaller predictionServiceCaller;
    final TeamDataCaller teamDataCaller;

    public ServiceController(TeamNameRepository teamNameRepository, PredictionServiceCaller predictionServiceCaller, TeamDataCaller teamDataCaller) {
        this.teamNameRepository = teamNameRepository;
        this.predictionServiceCaller = predictionServiceCaller;
        this.teamDataCaller = teamDataCaller;
    }

    @PostMapping(value = "/predict", produces = "application/json")
    public ResponseEntity<PredictionResponse> predict(@RequestBody PredictionRequest predictionRequest) throws InvalidServiceRequestException, InternalServiceException {
        if (teamNameRepository.invalidTeamName(predictionRequest.getLeagueInfo().getHomeTeam())
                || teamNameRepository.invalidTeamName(predictionRequest.getLeagueInfo().getAwayTeam())) {
            log.warn("Team name are not registered in cache. Try Again.");
            throw new InvalidServiceRequestException("Home and/or away team is not recognized.");
        }

        PredictionResponse predictionResponse = predictionServiceCaller.predictMatch(predictionRequest);

        return ResponseEntity.ok(predictionResponse);
    }

    @GetMapping(value = "/teamData", produces = "application/json")
    public ResponseEntity<TeamDataResponse> getTeamData() {
        TeamDataResponse teamDataResponse = teamDataCaller.getTeamData();
        return ResponseEntity.ok(teamDataResponse);
    }

    @ExceptionHandler({InvalidServiceRequestException.class})
    public ResponseEntity<ServiceException> handleBadRequestException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ExceptionHandler({InternalServiceException.class})
    public ResponseEntity<ServiceException> handleInternalServiceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }
}
