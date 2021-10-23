package service.prediction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import service.exception.InternalServiceException;
import service.prediction.model.request.PredictionRequest;
import service.prediction.model.response.PredictionResponse;

@Slf4j
@Component
public class PredictionServiceCaller {

    private static final String ENDPOINT = "/prediction";

    @Autowired WebClient webClient;

    public PredictionResponse predictMatch(PredictionRequest predictionRequest) throws InternalServiceException {
        return callModelPredictionService(predictionRequest);
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
