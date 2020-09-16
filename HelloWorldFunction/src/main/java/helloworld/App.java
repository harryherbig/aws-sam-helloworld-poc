package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.json.JSONObject;

/** Handler for requests to Lambda function. */
public class App implements RequestHandler<SQSEvent, String> {

  public String handleRequest(final SQSEvent input, final Context context) {
    final JSONObject result =
        new JSONObject().put("message", "Forcing bad version to make hook fail!.").put("result", 500);
    System.out.println(
        "Hello World Function was invoked. My version is: "
            + System.getenv("AWS_LAMBDA_FUNCTION_VERSION"));
    if (input.getRecords() != null) {
      input.getRecords().stream()
          .filter(r -> r.getBody() != null)
          .findFirst()
          .ifPresent(
              rec -> {
                result.put("message", rec.getBody());
                result.put("result", 200);
              });
    }
    return result.toString();
  }
}
