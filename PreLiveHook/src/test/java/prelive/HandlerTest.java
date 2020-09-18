package prelive;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import prelive.model.HookEvent;

public class HandlerTest {

  public void works() {

    final TestContext testContext = new TestContext();
    final Handler handler = new Handler();
    final String s = handler.handleRequest(HookEvent.forTest(), testContext);
    System.out.println("Test Response was: " + s);
    assertTrue(s.contains("SUCCESS"));
  }

  public void testAreExecuted() throws IOException {
    File myObj = new File("test_executed.txt");
    if (myObj.createNewFile()) {
      System.out.println("File created: " + myObj.getName());
    } else {
      System.out.println("File already exists.");
    }
  }
}
