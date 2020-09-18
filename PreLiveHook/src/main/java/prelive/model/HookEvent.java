package prelive.model;

import java.util.Objects;

/** { "deploymentId": "string", "lifecycleEventHookExecutionId": "string", "status": "string" } */
public class HookEvent {
  String deploymentId;
  String lifecycleEventHookExecutionId;
  String status;

  public HookEvent() {}

  public HookEvent(String deploymentId, String lifecycleEventHookExecutionId, String status) {
    this.deploymentId = deploymentId;
    this.lifecycleEventHookExecutionId = lifecycleEventHookExecutionId;
    this.status = status;
  }

  public static HookEvent forTest() {
    return new HookEvent("deploymentId", "lifecycleEventHookExecutionId", "status");
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getLifecycleEventHookExecutionId() {
    return lifecycleEventHookExecutionId;
  }

  public String getStatus() {
    return status;
  }

  public void setDeploymentId(String deploymentId) {
    this.deploymentId = deploymentId;
  }

  public void setLifecycleEventHookExecutionId(String lifecycleEventHookExecutionId) {
    this.lifecycleEventHookExecutionId = lifecycleEventHookExecutionId;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "{"
        + "deploymentId='"
        + deploymentId
        + '\''
        + ", lifecycleEventHookExecutionId='"
        + lifecycleEventHookExecutionId
        + '\''
        + ", status='"
        + status
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HookEvent hookEvent = (HookEvent) o;
    return deploymentId.equals(hookEvent.deploymentId)
        && lifecycleEventHookExecutionId.equals(hookEvent.lifecycleEventHookExecutionId)
        && status.equals(hookEvent.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deploymentId, lifecycleEventHookExecutionId, status);
  }
}
