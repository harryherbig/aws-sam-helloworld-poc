package prelive;

import com.amazonaws.regions.Regions;
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

public class Validator implements RequestHandler<HookEvent, String> {

  public Validator() {}

  @Override
  public String handleRequest(HookEvent input, Context context) {
    HookEvent mapped;
    final HookEvent testEvent = HookEvent.forTest();
    if (input != null && input.lifecycleEventHookExecutionId != null) {
      System.out.println("Got following event input:");
      System.out.println(input);
      mapped = input;
    } else {
      mapped = testEvent;
      System.out.println("Got NO !!!! input:");
    }

    final String sam_version = System.getenv("SAM_VERSION");
    final String sam_local = System.getenv("AWS_SAM_LOCAL");

    String funcName = Configs.LIVE_VERSION_ARN;
    if (sam_version == null || !sam_version.startsWith("arn:")) {
      System.out.println("###### LOCAL mode, invoking hard coded version arn:");
      System.out.println("###### " + funcName);
      System.out.println("######");
    } else {
      System.out.println("###### PROD  mode, invoking ENV version arn: ");
      System.out.println("###### " + sam_version);
      System.out.println("######");
      funcName = sam_version;
    }

    String region =
        System.getenv("AWS_REGION") != null
            ? System.getenv("AWS_REGION")
            : Regions.EU_WEST_1.getName();

    if (mapped.equals(testEvent)) {
      return "Test did more or less nothing but some parsing, but yay, SUCCESS";
      } else {
      InvokeRequest invokeRequest =
          new InvokeRequest()
              .withFunctionName(funcName)
              .withInvocationType(InvocationType.RequestResponse)
              .withPayload(Configs.SQS_EVENT_JSON);

      try {
        AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withRegion(region).build();
        InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
        final JSONObject result = new JSONObject(invokeResult.getPayload());
        final Try<Integer> resultTry = Try.of(() -> result.getInt("result"));
        if (resultTry.isSuccess() && resultTry.get() == 200) {
          final String message = result.getString("message");
          final String resultMessage =
              String.format("Result, with status: %s, message: %s", resultTry.get(), message);
          System.out.println("Got: " + resultMessage);
          return resultMessage;
          //        final PutLifecycleEventHookExecutionStatusRequest
          // putLifecycleEventHookExecutionStatusRequest = new
          // PutLifecycleEventHookExecutionStatusRequest();
          //        putLifecycleEventHookExecutionStatusRequest.setDeploymentId(
          //        codeDeploy.putLifecycleEventHookExecutionStatus()
        }

        return "Something went wrong, didn't get a json payload. Not switching Alias";
      } catch (ServiceException e) {
        System.out.println(e.getMessage());
        throw new RuntimeException("Validator failed to validate actual function");
      }
    }
  }
}
