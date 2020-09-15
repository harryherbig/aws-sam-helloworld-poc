# sam-poc-helloworld


## tl;dr (build, test and deploy)
  
`sam build`
  
`sam local invoke --event events/sqsevent.json`
  
`sam deploy`


## how to destroy everything?

`aws cloudformation delete-stack --stack-name sam-poc-harry`

<br/><br/>
beware, this leaves a stack, that is created before invoking `sam deploy`:

`aws-sam-cli-managed-default`

  
  
## IntelliJ

import both build.gradle files via rightclick


## Comments
### 15.09.
- Musste >10 mal den cloudformation stack abräumen
- manuell - per aws cli
- weil eine der beiden Functions nicht geupdated werden konnte


  
