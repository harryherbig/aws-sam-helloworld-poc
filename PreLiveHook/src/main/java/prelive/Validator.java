package prelive;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.nio.charset.StandardCharsets;

public class Validator implements RequestHandler<String, String> {

  @Override
  public String handleRequest(String input, Context context) {

    InvokeRequest invokeRequest =
        new InvokeRequest()
            .withFunctionName(System.getenv("NewVersion"))
            .withPayload(
                "{\n"
                    + "  \"Records\": [\n"
                    + "    {\n"
                    + "      \"messageId\" : \"MessageID_1\",\n"
                    + "      \"receiptHandle\" : \"MessageReceiptHandle\",\n"
                    + "      \"body\" : \"Message Body of Test Event\",\n"
                    + "      \"md5OfBody\" : \"a\",\n"
                    + "      \"md5OfMessageAttributes\" : \"a\",\n"
                    + "      \"eventSourceARN\": \"a\",\n"
                    + "      \"eventSource\": \"aws:sqs\",\n"
                    + "      \"awsRegion\": \"us-west-2\",\n"
                    + "      \"attributes\" : {\n"
                    + "        \"ApproximateReceiveCount\" : \"2\",\n"
                    + "        \"SentTimestamp\" : \"1520621625029\",\n"
                    + "        \"SenderId\" : \"AROAIWPX5BD2BHG722MW4:sender\",\n"
                    + "        \"ApproximateFirstReceiveTimestamp\" : \"1520621634884\"\n"
                    + "      },\n"
                    + "      \"messageAttributes\" : {}\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}");

    try {
      AWSLambda awsLambda =
          AWSLambdaClientBuilder.standard()
              .withCredentials(new ProfileCredentialsProvider())
              .withRegion(Regions.EU_WEST_1)
              .build();
      InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
      String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
      System.out.println(ans);
      return String.format("Result: status: %s, payload: %s", invokeResult.getStatusCode(), ans);
    } catch (ServiceException e) {
      System.out.println(e);
    }
    return "Failed";
  }
}
