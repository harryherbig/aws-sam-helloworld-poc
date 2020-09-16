package prelive;

public class Configs {
  final public static String LIVE_VERSION_ARN = "arn:aws:lambda:eu-west-1:763984976250:function:sam-poc-helloworld:live";

  final public static String SQS_EVENT_JSON = "{\n"
      + "  \"Records\": [\n"
      + "    {\n"
      + "      \"messageId\" : \"MessageID_1\",\n"
      + "      \"receiptHandle\" : \"MessageReceiptHandle\",\n"
      + "      \"body\" : \"A message from your pre live Hook Lambda\",\n"
      + "      \"md5OfBody\" : \"a\",\n"
      + "      \"md5OfMessageAttributes\" : \"a\",\n"
      + "      \"eventSourceARN\": \"a\",\n"
      + "      \"eventSource\": \"aws:sqs\",\n"
      + "      \"awsRegion\": \"us-west-2\",\n"
      + "      \"attributes\" : {\n"
      + "        \"ApproximateReceiveCount\" : \"2\",\n"
      + "        \"SentTimestamp\" : \"1520621625029\",\n"
      + "        \"SenderId\" : \"AROAIWPX5BD2BHG722MW4:sender\",\n"
      + "        \"ApproximateFirstReceiveTimestamp\" : \"1520621634884\"\n"
      + "      },\n"
      + "      \"messageAttributes\" : {}\n"
      + "    }\n"
      + "  ]\n"
      + "}";

}
