package service.prediction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import service.exception.InternalServiceException;
import service.prediction.model.request.PredictionRequest;
import service.prediction.model.response.PredictionForecast;
import service.prediction.model.response.PredictionResponse;
import service.prediction.model.response.Score;

import static common.Constants.LOCAL;
import static common.Constants.STAGE_KEY;
import static common.RandomNumberGenerator.generateRandomFloat;
import static common.RandomNumberGenerator.generateRandomInt;

@Slf4j
@Component
public class PredictionServiceCaller {

    private static final String ENDPOINT = "/prediction";
    private static final int UPPER_BOUND_SCORE = 5;
    private static final float UPPER_BOUND_FORECAST = 1.0f;
    private static final float UPPER_BOUND_EXPECTED_GOALS = 5.0f;

    final WebClient webClient;
    final Environment env;

    public PredictionServiceCaller(WebClient webClient, Environment env) {
        this.webClient = webClient;
        this.env = env;
    }

    public PredictionResponse predictMatch(PredictionRequest predictionRequest) throws InternalServiceException {
        return LOCAL.equalsIgnoreCase(env.getProperty(STAGE_KEY))
                ? generateMockPrediction()
                : callModelPredictionService(predictionRequest);
    }

    private PredictionResponse generateMockPrediction() {
        PredictionForecast predictionForecast = PredictionForecast.builder()
                .homeWin(generateRandomFloat(UPPER_BOUND_FORECAST))
                .awayWin(generateRandomFloat(UPPER_BOUND_FORECAST))
                .tie(generateRandomFloat(UPPER_BOUND_FORECAST))
                .build();
        Score score = Score.builder()
                .home(generateRandomInt(UPPER_BOUND_SCORE))
                .away(generateRandomInt(UPPER_BOUND_SCORE))
                .expectedHome(generateRandomFloat(UPPER_BOUND_EXPECTED_GOALS))
                .expectedAway(generateRandomFloat(UPPER_BOUND_EXPECTED_GOALS))
                .build();
        return PredictionResponse.builder()
                .forecast(predictionForecast)
                .score(score)
                .build();
    }

    private PredictionResponse callModelPredictionService(PredictionRequest predictionRequest) throws InternalServiceException {
        try {
            Mono<PredictionResponse> response = webClient
                    .method(HttpMethod.POST)
                    .uri(ENDPOINT)
                    .bodyValue(predictionRequest)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            log.info("Received 2xx exception from Model Prediction Service.");
                            return clientResponse.bodyToMono(PredictionResponse.class);
                        } else if (clientResponse.statusCode().is4xxClientError()) {
                            log.info("Received 4xx exception from Model Prediction Service.");
                            return clientResponse.createException().flatMap(Mono::error);
                        } else {
                            log.info("Received 5xx exception from Model Prediction Service.");
                            return clientResponse.createException().flatMap(Mono::error);
                        }
                    });
            return response.block();
        } catch (Exception e) {
            log.error("Exception occurred during call to Model Prediction Service, more details here: ", e);
            throw new InternalServiceException("Exception occurred during call to Model Prediction Service");
        }
    }
}
