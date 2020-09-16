# sam-poc-helloworld


## tl;dr (build, test and deploy)
### Single Steps  
`sam build`
  
`sam local invoke --event events/sqsevent.json`
  
`sam deploy`

### First run
`sam build --use-container && sam deploy`


:warning: <br/>
changes exclusively to template.yml also need a `sam build` before able to `sam deploy` it.


### consecutive deploys of new versions
```
sam build --use-container && \
sam package \
   --template-file template.yaml \
   --output-template-file deployment.yml --s3-bucket sam-poc-deployment-artifacts && \
sam deploy  --template-file deployment.yml --stack-name sam-poc-harry
```

## how to destroy everything?
1. clean s3 buckets to prevent failed deletions 
2. `aws cloudformation delete-stack --stack-name sam-poc-harry`

<br/><br/>
beware, this leaves a stack, that is created by invoking `sam init`:

`aws-sam-cli-managed-default`

this manages template versions, comparable to a TF remote state

  
  
## IntelliJ

import both build.gradle files via right-click menu


## Comments
### 15.09.
- Musste >10 mal den cloudformation stack per aws cli abräumen
- weil eine der beiden Functions nicht geupdated werden konnte


### 16.09.
obwohl `sam validate`d treten Fehler bei `sam deploy` auf, und die Hilfe zur Fehlerbehebung ist dürftig, da es nicht mal Zeilenangaben gibt.
See: 
<br>
`Waiting for changeset to be created..`<br>
`Error: Failed to create changeset for the stack: sam-poc-harry,`<br>
`ex: Waiter ChangeSetCreateComplete failed:`<br>
`Waiter encountered a terminal failure state Status: FAILED. Reason: Template error: every Fn::GetAtt object requires two non-empty parameters, the resource name and the resource attribute`
 

die pre live hook lambda konnte nicht gestartet werden, weil die Handler class nicht gefunden wurde, dadurch lief das Deployment endlos und ich musste es per Hand abbrechen
 
 
oft failed Update `HelloWorldFunctionAlias`, was nur durch `aws cf delete-stack...` repariert werden kann 


Conventional Namings, e.g. for PreHook Lambda, sonst stimmen die Rechte nicht
Name für statebucket nicht änderbar
Keine custom tags vergebbar


IntelliJ rename package "helloworld" of `HelloWorldFunction` also renames package `prelive` for `PreLiveHook`