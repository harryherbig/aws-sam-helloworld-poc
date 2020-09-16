package prelive;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.codedeploy.AmazonCodeDeploy;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClientBuilder;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.vavr.control.Try;
import org.json.JSONObject;

public class Validator implements RequestHandler<String, String> {
  AmazonCodeDeploy codeDeploy;
  public Validator() {
    codeDeploy = AmazonCodeDeployClientBuilder.defaultClient();
  }

  @Override
  public String handleRequest(String input, Context context) {
    if (input!=null && input.contains("{")) {
      final JSONObject jsonObject = new JSONObject(input);
      System.out.println("Got following event input:");
      System.out.println(jsonObject.toString());
    }  else {
      System.out.println("Got NO !!!! input:");
    }

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
            .withInvocationType(InvocationType.RequestResponse)
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
      final JSONObject result = new JSONObject(invokeResult.getPayload());
      final Try<Integer> resultTry = Try.of(() -> result.getInt("result"));
      if ( resultTry.isSuccess() && resultTry.get() == 200) {
        final String message = result.getString("message");
        return String.format("Result, with status: %s, message: %s", resultTry.get(),
            message);
//        final PutLifecycleEventHookExecutionStatusRequest putLifecycleEventHookExecutionStatusRequest = new PutLifecycleEventHookExecutionStatusRequest();
//        putLifecycleEventHookExecutionStatusRequest.setDeploymentId(
//        codeDeploy.putLifecycleEventHookExecutionStatus()
      }

      return "Something went wrong, didn't get a json payload. Not switching Alias";
    } catch (ServiceException e) {
      System.out.println(e.getMessage());
    }
    return "Failed";
  }
}
