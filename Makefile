.PHONY: help

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help

init: sam-build-container sam-deploy-all ## run when no cloudformation stack exists to deploy current state


destroy: cf-cancel-stackupdate s3-empty ## deletes sam cf stack via aws cli after emptying the s3 bucket
	aws cloudformation delete-stack --stack-name sam-poc-harry

update: sam-build-container sam-package-all sam-deploy-packaged-all ## run when cf stack exists to trigger a CodeDeploy Deployment

invoke-local-hook: sam-build-container ## locally invoke PreLiveHook with Test Event (needs aws credentials)
	sam local invoke --profile default PreLiveHook --event events/prelivehook.json

invoke-local-function: sam-build-container ## locally invoke HelloWorldFunction with SQS Test Event
	sam local invoke HelloWorldFunction --event events/sqsevent.json


cf-cancel-stackupdate:
	@if [ "$(aws cloudformation describe-stacks --stack-name sam-poc-harry | jq ".Stacks[0].StackStatus" --raw-output | sed -En 's/[A-Z_]+_(IN_PROGRESS)/\1/p')" = "IN_PROGRESS" ]; then aws cloudformation cancel-update-stack --stack-name sam-poc-harry; sleep 5;fi

sam-build-container:
	sam build --use-container

sam-deploy-all:
	sam deploy

sam-package-all: ## create deployment.yml and upload artifacts to s3 via `sam package`
	sam package --template-file template.yaml --output-template-file deployment.yml --s3-bucket sam-poc-deployment-artifacts

cf-package-all: ## create cf-template-packaged.yaml and upload artifacts to s3 via `aws cf package`
	aws cloudformation package --template-file template.yaml --s3-bucket sam-poc-deployment-artifacts --output-template-file cf-template-packaged.yaml

sam-deploy-packaged-all:
	sam deploy --template-file deployment.yml --stack-name sam-poc-harry

s3-empty:
	aws s3 rm s3://sam-poc-deployment-artifacts --recursive

ls-hook: ## ls compile result of PreLiveHook
	ls -al .aws-sam/build/PreLiveHook

ls-function: ## ls compile result of HelloWorldFunction
	ls -al .aws-sam/build/HelloWorldFunction

