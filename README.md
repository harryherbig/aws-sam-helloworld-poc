# sam-poc-helloworld


## tl;dr (build, test and deploy)
  
`sam build`
  
`sam local invoke --event events/sqsevent.json`
  
`sam deploy`


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


  
