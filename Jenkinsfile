// Variables defined 

def RELEASE_BRANCH_NAME = "master"
def emailNotifications = 'anjesh.kumar@wipro.com'
def notificationSent    = false
def workspace = env.WORKSPACE
def bucket = 'awslambdadev'
def FUNCTION_NAME = 'awslambdausingcli'


pipeline {
    //Default agent label to run all stages
    agent { label 'master'}
   
    //Default tools for agent
    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
       
    }
    
    options {
        //Disable concurrentbuilds for the same job
        disableConcurrentBuilds()
        // Colorize the console log
        ansiColor("xterm")          
        // Add timestamps to console log
        timestamps()
        
    }
  
  stages {

    stage('SCM Checkout') {
      steps {
        script {
            // Make sure we're in a clean space
           deleteDir()
           // Check out revision that was used to fetch the Jenkinsfile running the pipeline
          checkout scm
            echo "PATH = ${PATH}"
          echo 'Stage 1'
        }
      }
    }

    stage('Build Lamba') {
      steps {
        script {
          bat 'mvn clean install'        
        
        }
      }
    }
      
    stage('Push') {
      steps {
        script {
          ARTIFACTID = readMavenPom().getArtifactId()
           VERSION = readMavenPom().getVersion()
           echo "ARTIFACTID: ${ARTIFACTID}"
           echo "VERSION: ${VERSION}"
           JARNAME = ARTIFACTID+'.'+VERSION+'.jar'
           echo "JARNAME: ${JARNAME}"
            echo" workspace: ${env.WORKSPACE}"
           DIR = pwd();
            echo "DIR: ${DIR}"
           
          echo 'Stage 2 : ${env.WORKSPACE}'
           
          withAWS(region:'us-east-1',credentials:'AWS_Credentials') {
           bat 'aws s3 ls '      
          // bat "aws s3api put-object --bucket anjeshlambdatest --key com.aws.hellolambda.example-1.0.0.jar --body '${env.WORKSPACE}/target/com.aws.hellolambda.example-1.0.0.jar'"
         }
                
          echo 'Stage 3'      
        
        }
      }
    }
      
   stage('Integration Test') {
     steps {
       script {
           ARTIFACTID = readMavenPom().getArtifactId()
           VERSION = readMavenPom().getVersion()
            label =    does_lambda_exist()  
            echo "Stage 3 : ${label}"
           if( label == 'true' ) {
                echo "Stage 2 Yes"
               withAWS(region:'us-east-1',credentials:'AWS_Credentials') {
                   // sh "aws lambda delete-function --function-name awslambdausingcli"
                  sh "aws lambda update-function-code --function-name awslambdausingcli --s3-bucket anjeshlambdatest --s3-key com.aws.hellolambda.example-1.0.0.jar"
                  sh 'aws lambda invoke --function-name awslambdausingcli --payload "{""name"": ""anjesh""}" outputfile.txt'
               }
            }  else{
               echo "Stage 2 No"
            }
          
        }
      }
    }
      
      stage('Deploy') {
      steps {
        script {
        label =    does_lambda_exist()  
            echo "Stage 3 : ${label}"
        }
      }
    }

  } // Stages
} // Pipeline

def does_lambda_exist() {	
  isexist='true'
 // bat  'aws lambda get-function --function-name awslambdausingcli > /dev/null 2>&1'
   
  return isexist
}
