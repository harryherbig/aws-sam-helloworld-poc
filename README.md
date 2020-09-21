# sam-poc-helloworld

## TODO
- add "make target" to stop deployment and rollback stack (dont want to wait 1h) 

## tl;dr (build, test and deploy)
### From scratch
`sam init`

`make create`

### consecutive deploys of new versions
`make update`

## how to destroy everything?
`make destroy`
<br/>

- beware, this leaves a stack, that is created by invoking "sam init" `aws-sam-cli-managed-default`
- the bucket name could probably be set in the `samconfig.toml`
- this bucket manages template versions, comparable to a TF remote state

if you really want to clean as if you were never here before:

`make wipe`



  
  
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

F*CkIT#@%": pre hook geht einfach nur mit node.js, in Java hat der Event keine Felder. Type ist auch unbekannt. 

Okay: geht doch mit Java Hook Lambda, wenn man `Map<String, String> event` als Input nimmt.

## Links
https://github.com/aws-samples/aws-safe-lambda-deployments
https://github.com/aws/aws-lambda-builders/