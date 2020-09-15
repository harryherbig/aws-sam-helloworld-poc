package prelive;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.nio.charset.StandardCharsets;

public class Validator implements RequestHandler<Void, String> {

  @Override
  public String handleRequest(Void input, Context context) {
    final String sam_version = System.getenv("SAM_VERSION");
    final String sam_local = System.getenv("AWS_SAM_LOCAL");
    final boolean local = sam_local!= null && sam_local.equals("true");

    String funcName = "arn:aws:lambda:eu-west-1:763984976250:function:sam-poc-helloworld:$LATEST";
    if (local) {
      System.out.println("###### LOCAL, invoking hard coded value:");
      System.out.println("###### " + funcName);
    } else {
      System.out.println("######");
      System.out.println("SAM_VERSION: " + sam_version);
      System.out.println("######");
      if (sam_version != null && sam_version.contains("arn:")) {
        funcName = sam_version;
      } else {
        System.out.println("###### PROD MODE, but SAM_VERSION not present");
      }
    }

    String region =
        System.getenv("AWS_REGION") != null
            ? System.getenv("AWS_REGION")
            : Regions.EU_WEST_1.getName();

    InvokeRequest invokeRequest =
        new InvokeRequest()
            .withFunctionName(funcName)
            .withPayload(
                "{\n"
                    + "  \"Records\": [\n"
                    + "    {\n"
                    + "      \"messageId\" : \"MessageID_1\",\n"
                    + "      \"receiptHandle\" : \"MessageReceiptHandle\",\n"
                    + "      \"body\" : \"A message from your pre live Hook Lambda\",\n"
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
      AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withRegion(region).build();
      InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
      String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
      System.out.println(ans);
      return String.format("Result: status: %s, payload: %s", invokeResult.getStatusCode(), ans);
    } catch (ServiceException e) {
      System.out.println(e.getMessage());
    }
    return "Failed";
  }
}
