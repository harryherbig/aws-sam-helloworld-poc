package prelive;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.json.JSONObject;
import prelive.internal.Configs;
import prelive.internal.FunctionInvoker;
import prelive.internal.LifeCycleManager;
import prelive.model.HookEvent;

public class Handler implements RequestHandler<HookEvent, String> {

  public Handler() {}

  @Override
  public String handleRequest(HookEvent input, Context context) {
    boolean isTest = System.getenv("AWS_SAM_LOCAL") != null;

    if (isTest) {
      System.out.println("TEST MODE");
      final JSONObject result = FunctionInvoker.invoke(Configs.LIVE_VERSION_ARN);
      return "Finished TEST MODE: invoked live alias with result: " + result.toString();
    } else {
      System.out.println("PROD MODE");
      final Option<String> maybeExecId = Option.of(input.getLifecycleEventHookExecutionId());
      final Option<String> maybeDeploymentId = Option.of(input.getDeploymentId());
      final String newVersionFromEnv = System.getenv("SAM_VERSION");
      final JSONObject result = FunctionInvoker.invoke(newVersionFromEnv);
      final boolean cycle =
          LifeCycleManager.manageLifeCycle(
              maybeExecId.get(), maybeDeploymentId.get(), isResultSuccess(result));
      return "Finished Handler in PROD mode, lifecycle done: "
          + (cycle ? "YESS ERRMAGERD" : "total disaster happened");
    }
  }

  boolean isResultSuccess(JSONObject result) {
    final Try<Integer> resultTry = Try.of(() -> result.getInt("result"));
    final String message = result.getString("message");
    if (resultTry.isSuccess() && resultTry.get() == 200) {
      System.out.println(String.format("SUCCESS with message: %s", message));
      return true;
    } else {
      System.out.println(String.format("FAILURE with message: %s", message));
      return false;
    }
  }
}
