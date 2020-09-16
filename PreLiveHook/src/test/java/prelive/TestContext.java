package prelive;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestContext implements Context {
  public TestContext() {}

  public String getAwsRequestId() {
    return new String("495b12a8-xmpl-4eca-8168-160484189f99");
  }

  public String getLogGroupName() {
    return new String("/aws/lambda/fake-function");
  }

  @Override
  public String getLogStreamName() {
    return "2020/08/25/[$LATEST]4c54160808c6406d97870cb66f7727aa";
  }

  @Override
  public String getFunctionName() {
    return "FunctionName";
  }

  @Override
  public String getFunctionVersion() {
    return "0.0.0";
  }

  @Override
  public String getInvokedFunctionArn() {
    return "arn::arn";
  }

  @Override
  public CognitoIdentity getIdentity() {
    return null;
  }

  @Override
  public ClientContext getClientContext() {
    return null;
  }

  @Override
  public int getRemainingTimeInMillis() {
    return 1000;
  }

  @Override
  public int getMemoryLimitInMB() {
    return 256;
  }

  public LambdaLogger getLogger() {
    return new LambdaLogger() {

      public void log(String message) {
        System.out.println(message);
      }

      public void log(byte[] message) {
        System.out.println(message);
      }
    };
  }
}
