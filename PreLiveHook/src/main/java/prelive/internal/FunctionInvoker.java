package prelive.internal;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import org.json.JSONObject;

public class FunctionInvoker {

  private static String region;

  static {
    region =
        System.getenv("AWS_REGION") != null
            ? System.getenv("AWS_REGION")
            : Regions.EU_WEST_1.getName();
  }

  public static JSONObject invoke(String funcName) {
    InvokeRequest invokeRequest =
        new InvokeRequest()
            .withFunctionName(funcName)
            .withInvocationType(InvocationType.RequestResponse)
            .withPayload(Configs.SQS_EVENT_JSON);

    AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withRegion(region).build();
    InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
    return new JSONObject(invokeResult.getPayload());
  }
}
