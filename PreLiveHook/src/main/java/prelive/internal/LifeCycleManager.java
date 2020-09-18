package prelive.internal;

import com.amazonaws.services.codedeploy.AmazonCodeDeploy;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient;
import com.amazonaws.services.codedeploy.model.PutLifecycleEventHookExecutionStatusRequest;

public class LifeCycleManager {
  private static AmazonCodeDeploy client;

  static {
    client = AmazonCodeDeployClient.builder().build();
  }

  public static boolean manageLifeCycle(String deploymentId, String execId, boolean success) {
    var request = new PutLifecycleEventHookExecutionStatusRequest();
    request.setDeploymentId(deploymentId);
    request.setLifecycleEventHookExecutionId(execId);
    try {
      client.putLifecycleEventHookExecutionStatus(request);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
