package prelive.internal;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import java.nio.charset.StandardCharsets;
import prelive.InvocationResponse;

public class FunctionInvoker {

  private static String region;

  static {
    region =
        System.getenv("AWS_REGION") != null
            ? System.getenv("AWS_REGION")
            : Regions.EU_WEST_1.getName();
  }

  public static InvocationResponse invoke(String arn) {
    InvokeRequest invokeRequest =
        new InvokeRequest()
            .withFunctionName(arn)
            .withInvocationType(InvocationType.RequestResponse)
            .withPayload("{\"greeting\":\"Hello Lambda\"}");

    AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withRegion(region).build();
    InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
    final String payloadAsString =
        new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
    return InvocationResponse.with(payloadAsString, invokeResult.getStatusCode());
  }
}
