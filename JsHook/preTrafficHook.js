/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

'use strict';

const AWS = require('aws-sdk');
const codedeploy = new AWS.CodeDeploy({apiVersion: '2014-10-06'});
let lambda = new AWS.Lambda();

console.log("PreTraffic is on the job!");
exports.handler = (event, context, callback) => {

  console.log("Entering PreTraffic Hook!");

  // Read the DeploymentId & LifecycleEventHookExecutionId from the event payload
  let deploymentId = event.DeploymentId;
  let lifecycleEventHookExecutionId = event.LifecycleEventHookExecutionId;

  let functionToTest = process.env.NewVersion;
  console.log("Testing new function version: " + functionToTest);

  // Perform validation of the newly deployed Lambda version
  let lambdaParams = {
    FunctionName: functionToTest,
    InvocationType: "RequestResponse"
  };

  let lambdaResult = "Failed";
  lambda.invoke(lambdaParams, function (err, data) {
    if (err) {	// an error occurred
      console.log(err, err.stack);
      lambdaResult = "Failed";
    } else {	// successful response
      // lets skip payload parsing for now because of my lack of js skills
      lambdaResult = "Succeeded";
      console.log("Validation testing succeeded!");

      // Complete the PreTraffic Hook by sending CodeDeploy the validation status
      let params = {
        deploymentId: deploymentId,
        lifecycleEventHookExecutionId: lifecycleEventHookExecutionId,
        status: lambdaResult // status can be 'Succeeded' or 'Failed'
      };

      // Pass AWS CodeDeploy the prepared validation test results.
      codedeploy.putLifecycleEventHookExecutionStatus(params,
          function (err, data) {
            if (err) {
              // Validation failed.
              console.log('CodeDeploy Status update failed');
              console.log(err, err.stack);
              callback("CodeDeploy Status update failed");
            } else {
              // Validation succeeded.
              console.log('Codedeploy status updated successfully');
              callback(null, 'Codedeploy status updated successfully');
            }
          });
    }
  });
}