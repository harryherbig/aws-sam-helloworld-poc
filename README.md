# sam-poc-helloworld


## tl;dr (build, test and deploy)
  
`sam build`
  
`sam local invoke --event events/sqsevent.json`
  
`sam deploy`

:warning: <br/>
changes exclusively to template.yml also need a `sam build` before able to `sam deploy` it.




## deploy new version like a merged PR
`sam package \
   --template-file template.yaml \
   --output-template-file deployment.yml --s3-bucket sam-poc-deployment-artifacts`
   
`sam deploy --template-file deployment.yml --stack-name sam-poc-harry`

## how to destroy everything?
1. clean s3 buckets to prevent failed deletions 
2. `aws cloudformation delete-stack --stack-name sam-poc-harry`

<br/><br/>
beware, this leaves a stack, that is created by invoking `sam init`:

`aws-sam-cli-managed-default`

this manages template versions, like a TF remote state

  
  
## IntelliJ

import both build.gradle files via rightclick


## Comments
### 15.09.
- Musste >10 mal den cloudformation stack abräumen
- manuell - per aws cli
- weil eine der beiden Functions nicht geupdated werden konnte


### 16.09.
obwohl `sam validate`d treten alle Fehler erst bei `sam deploy` auf, und die Hilfe zur Fehlerbehebung ist dürftig, da nicht mal die Zeile genannt wird, in der der Fehler auftritt:
<br>
`Waiting for changeset to be created..`<br>
`Error: Failed to create changeset for the stack: sam-poc-harry,`<br>
`ex: Waiter ChangeSetCreateComplete failed:`<br>
`Waiter encountered a terminal failure state Status: FAILED. Reason: Template error: every Fn::GetAtt object requires two non-empty parameters, the resource name and the resource attribute`
 

  
