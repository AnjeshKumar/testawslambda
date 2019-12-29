 #!/bin/bash

FUNCTION_NAME=$1
AWS_LAMBDA_ROLE=$2
HANDLER_NAME=$3

echo $FUNCTION_NAME
echo $AWS_LAMBDA_ROLE
echo $HANDLER_NAME

function does_lambda_exist() {	
  isexist='true'
  aws lambda get-function --function-name $1 > /dev/null 2>&1
  if [ 0 -eq $? ]; then
    echo "Lambda '$1' exists"
    isexist='true'
  else
    echo "Lambda '$1' does not exist"
    isexist='false'
  fi  
}

does_lambda_exist $FUNCTION_NAME

echo $result

if [ "$isexist" = "true" ]; then
    echo "Strings aa are equal."
    aws lambda create-function --function-name "$1" --runtime "java8" --role "$2" --handler "$3" --timeout 5 --memory-size 256 --zip-file "fileb://target/com.aws.hellolambda.example-1.0.0.jar"
else
   aws lambda create-function --function-name "$1" --runtime "java8" --role "$2" --handler "$3" --timeout 5 --memory-size 256 --zip-file "fileb://../target/com.aws.hellolambda.example-1.0.0.jar"
fi
