package prelive;

public class InvocationResponse {

  private String hello;
  private int invocationStatusCode;
  boolean success = false;

  public InvocationResponse(String hello, int invocationStatusCode) {
    this.hello = hello;
    this.invocationStatusCode = invocationStatusCode;
    if (invocationStatusCode == 200 && hello.contains("200")) success = true;
  }

  public static InvocationResponse with(String message, int invocationStatusCode) {
    return new InvocationResponse(message, invocationStatusCode);
  }

  @Override
  public String toString() {
    return "[" + invocationStatusCode + "] " + hello;
  }
}
