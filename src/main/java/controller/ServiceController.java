package controller;

import cache.teamName.TeamNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.exception.InternalServiceException;
import service.exception.InvalidServiceRequestException;
import service.exception.ServiceException;
import service.prediction.PredictionServiceCaller;
import service.prediction.model.request.PredictionRequest;
import service.prediction.model.response.PredictionResponse;

@Slf4j
@RestController
public class ServiceController {

    @Autowired TeamNameRepository teamNameRepository;
    @Autowired PredictionServiceCaller predictionServiceCaller;

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

    @ExceptionHandler({InvalidServiceRequestException.class})
    public ResponseEntity<ServiceException> handleBadRequestException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ExceptionHandler({InternalServiceException.class})
    public ResponseEntity<ServiceException> handleInternalServiceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }
}
