package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.json.JSONObject;

/** Handler for requests to Lambda function. */
public class App implements RequestHandler<SQSEvent, String> {

  public String handleRequest(final SQSEvent input, final Context context) {
    final JSONObject OK =
        new JSONObject().put("message", "Harry says: All good in da hood!").put("statusCode", 200);
    final JSONObject BAD =
        new JSONObject()
            .put("message", "Harry forcing failure!.")
            .put("statusCode", 500);
    System.out.println(
        "Hello World Function was invoked. My version is: "
            + System.getenv("AWS_LAMBDA_FUNCTION_VERSION"));

//    return BAD.toString();
    return OK.toString();
  }
}
