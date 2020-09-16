package prelive;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class ValidatorTest {

  @Test
  public void works() {

    final TestContext testContext = new TestContext();
    final Validator validator = new Validator();
    final String s = validator.handleRequest(HookEvent.forTest(), testContext);
    System.out.println("Test Response was: " + s);
    assertTrue(s.contains("SUCCESS"));
  }

  @Test
  public void testAreExecuted() throws IOException {
    File myObj = new File("test_executed.txt");
    if (myObj.createNewFile()) {
      System.out.println("File created: " + myObj.getName());
    } else {
      System.out.println("File already exists.");
    }
  }
}
