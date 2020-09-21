package prelive.internal;

import com.amazonaws.services.codedeploy.AmazonCodeDeploy;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient;
import com.amazonaws.services.codedeploy.model.PutLifecycleEventHookExecutionStatusRequest;

public class LifeCycleManager {
  private static AmazonCodeDeploy client;

  static {
    client = AmazonCodeDeployClient.builder().build();
  }

  public static boolean manageLifeCycle(
      String deploymentId, String lifeCycleExecutionId, boolean success) {
    var request =
        new PutLifecycleEventHookExecutionStatusRequest()
            .withLifecycleEventHookExecutionId(lifeCycleExecutionId)
            .withDeploymentId(deploymentId)
            .withStatus(success ? "Succeeded" : "Failed");
    System.out.println("codedeploy request: " + request);
    try {
      System.out.println("START putting LifecycleEventHookExecutionStatus to codedeploy");
      client.putLifecycleEventHookExecutionStatus(request);
      System.out.println("DONE  putting LifecycleEventHookExecutionStatus to codedeploy");
      return true;
    } catch (Exception e) {
      System.out.println("failed with: " + e.getMessage());
      System.out.println("FAIL  putting LifecycleEventHookExecutionStatus to codedeploy");
      return false;
    }
  }
}
