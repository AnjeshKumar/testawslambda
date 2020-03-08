// Variables defined 

def RELEASE_BRANCH_NAME = "master"
def emailNotifications = 'anjesh.kumar@wipro.com'
def notificationSent    = false
def workspace = env.WORKSPACE
def bucket = 'awslambdadev'


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
      
   stage('Connect AWS') {
     steps {
       script {
           ARTIFACTID = readMavenPom().getArtifactId()
           VERSION = readMavenPom().getVersion()
           echo "ARTIFACTID: ${ARTIFACTID}"
           echo "VERSION: ${VERSION}"
           JARNAME = ARTIFACTID+'.'+VERSION+'.jar'
           echo "JARNAME: ${JARNAME}"
            echo "workspace: ${env.WORKSPACE}"
           DIR = pwd();
            echo "DIR: ${DIR}"
           
          echo 'Stage 2'
           
          withAWS(region:'us-east-1',credentials:'AWS_Credentials') {
           bat 'aws s3 ls'
           bat 'aws s3 cp /target/com.aws.hellolambda.example-1.0.0.jar s3://awslambdadev/'
         }
                
          echo 'Stage 3'
        }
      }
    }

  }
}
