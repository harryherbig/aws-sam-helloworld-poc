package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

/** Handler for requests to Lambda function. */
public class App implements RequestHandler<SQSEvent, String> {

  public String handleRequest(final SQSEvent input, final Context context) {
    final String fallbackReturn = "v1 Got empty input, can't parse record";
    if (input.getRecords() != null) {
      return input.getRecords().stream()
          .filter(r -> r.getBody() != null)
          .findFirst()
          .map(rec -> String.format("hello world v1, input was %s", rec.getBody()))
          .orElse(fallbackReturn);
    } else {
      return fallbackReturn;
    }
  }
}
