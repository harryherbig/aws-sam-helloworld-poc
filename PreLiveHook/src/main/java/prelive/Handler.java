package prelive;

import com.amazonaws.SdkClientException;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.protocol.MarshallingType;
import com.amazonaws.protocol.ProtocolMarshaller;
import com.amazonaws.protocol.StructuredPojo;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.Serializable;
import java.util.Map;
import prelive.internal.FunctionInvoker;
import prelive.internal.LifeCycleManager;


// PutLifecycleEventHookExecutionStatusResult
// LastDeploymentInfo

public class Handler implements RequestHandler<Map<String,String>, String> {

  public Handler() {}


  // {DeploymentId=d-NN167W3D6, LifecycleEventHookExecutionId=eyJlbmNyeXB0ZWREYXRhIjoiMWFjU2xMY2RyNS9EWHRJcjhRTkVBcnhoUDVMUUNTNlVKVG52empDUFE5aE1YaVNiRFBZUnBtQWx3NDNRL3lobW1qNnVLOHdKU3I5bFZpcHRMWXROeXRBbTFnbHNqMHZvVEJXekVRaGZoOGh3VHU2SnlUb0JqTk9pRGJNMjNBUXF6dFBOU0N0azlnPT0iLCJpdlBhcmFtZXRlclNwZWMiOiJZMjE0alJHcTRvQXZJZW1KIiwibWF0ZXJpYWxTZXRTZXJpYWwiOjF9}

  @Override
  public String handleRequest(Map<String,String> event, Context context) {
    final String newVersionFromEnv = System.getenv("SAM_NEW_VERSION");

    System.out.println("got event: "  + event.toString());

    String deploymentId = event.get("DeploymentId");
    String lifecycleEventHookExecutionId = event.get("LifecycleEventHookExecutionId");

    final InvocationResponse response = FunctionInvoker.invoke(newVersionFromEnv);
    System.out.println(
        String.format("invoked %s with result: %s", newVersionFromEnv, response.toString()));

    return "alias was set to new version: "
        + (LifeCycleManager.manageLifeCycle(
                deploymentId, lifecycleEventHookExecutionId, response.success)
            ? "YESS!"
            : "NO!, setting lifecycle status failed");
  }
}
