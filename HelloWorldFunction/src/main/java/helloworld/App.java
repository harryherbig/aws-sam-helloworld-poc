package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<SQSEvent, String> {

    public String handleRequest(final SQSEvent input, final Context context) {
        String eventBody = Optional.ofNullable(input.getRecords().stream().findFirst().map(SQSMessage::getBody).get()).orElse("empty input");
        return String.format("{ \"message\": \"hello world, input was %s\"}", eventBody);
    }
}