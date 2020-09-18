.PHONY: help

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help

# / MAIN SAM COMMANDS #
create: build deploy ## create and deploy sam stack from scratch

destroy: cf-cancel-stackupdate  ## destroy sam stack
	@echo "deleting stack via aws cf";
	aws cloudformation delete-stack --stack-name sam-poc-harry

update-via-sam: build package-via-sam sam-deploy-pre-packaged ## trigger a CodeDeploy Deployment for existing stack
update-via-cf: build package-via-cf sam-deploy-pre-packaged ## trigger a CodeDeploy Deployment for existing stack
# MAIN SAM COMMANDS / #

# ------------------------------- #

# / LOCAL TESTING #
invoke-hook-local: build ## locally invoke PreLiveHook with Test Event (needs aws credentials)
	sam local invoke --profile default PreLiveHook --event events/prelivehook.json

invoke-function-local: build ## locally invoke HelloWorldFunction with SQS Test Event
	sam local invoke HelloWorldFunction --event events/sqsevent.json
# LOCAL TESTING / #

# ------------------------------- #

# / SAM SINGLE STEPS #
SAM_TMPL="template.yaml"
DEPLOY_TMPL="deployment.yml"
STACK_NAME="sam-poc-harry"
build:
	sam build --use-container

deploy:
	sam deploy

package-via-sam: ## create deployment.yml manually and upload artifacts to s3 via `sam package`
	sam package --template-file $(SAM_TMPL) --output-template-file $(DEPLOY_TMPL) --s3-bucket $(BUCKET_STACKNAME)

package-via-cf: ## create cf-template-packaged.yaml manually  and upload artifacts to s3 via `aws cf package`
	aws cloudformation package --template-file $(SAM_TMPL) --output-template-file $(DEPLOY_TMPL) --s3-bucket $(BUCKET_STACKNAME)

sam-deploy-pre-packaged:
	sam deploy --template-file $(DEPLOY_TMPL) --stack-name $(STACKNAME)



# / DEBUG #
debug-buildFiles: debug-lsMainBuildFiles debug-lsHookBuildFiles ## list all build files

debug-ls-hook-build-files: # list PreLiveHook build files
	ls -alR .aws-sam/build/PreLiveHook/prelive

debug-ls-func-build-files: # list HelloWorld build files
	ls -al .aws-sam/build/HelloWorldFunction/helloworld

debug-get-artifact-name:
	@echo "$$(awk '/FunctionName: CodeDeployHook_PreLiveHook_HelloWorldFunction/{getline; print}' $(DEPLOY_TMPL) | sed -e 's/\s\{6,\}CodeUri\:\s//')"

debug-dl-artifact-from-s3:
	aws s3 cp $$(awk '/FunctionName: CodeDeployHook_PreLiveHook_HelloWorldFunction/{getline; print}' $(DEPLOY_TMPL) | sed -e 's/\s\{6,\}CodeUri\:\s//') dl_artifacts/
# DEBUG / #

# / S3 Bucket for deployments #
BUCKET_STACKNAME="sam-deployment-bucket"
create-bucket-stack: ## create the bucket for deployment packages
	aws cloudformation deploy --template-file sam-bucket.yaml --stack-name $(BUCKET_STACKNAME)

destroy-bucket-stack: s3-empty ## destroy the bucket for deployment packages
	aws cloudformation destroy --stack-name $(BUCKET_STACKNAME)
# S3 Bucket for deployments / #



# / HELPERS private functions#
s3-empty:
	aws s3 rm s3://sam-poc-deployment-artifacts --recursive

cf-cancel-stackupdate:
	@if [ "$$(aws cloudformation describe-stacks --stack-name $(STACKNAME) | jq '.Stacks[0].StackStatus' --raw-output | sed -En 's/[A-Z_]+_(IN_PROGRESS)/\1/p')" = "IN_PROGRESS" ]; then @echo "have to cancel update"; aws cloudformation cancel-update-stack --stack-name $(STACKNAME) ; @echo "sleeping 5s"; sleep 5; fi

# HELPERS / #