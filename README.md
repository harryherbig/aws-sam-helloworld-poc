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

 

  
