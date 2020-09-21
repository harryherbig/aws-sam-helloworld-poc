package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/** Handler for requests to Lambda function. */
public class App implements RequestHandler<Object, String> {

  public String handleRequest(final Object event, final Context context) {

    String data = event != null ? event.toString() : "{}";

    System.out.println(
        "Hello World Function was invoked. My version is: "
            + System.getenv("AWS_LAMBDA_FUNCTION_VERSION"));

    System.out.println("hello from harry");
    System.out.println("data as string is: " + data);

    return "200 OK - " + data;
    //    return "500 Error";
  }
}
