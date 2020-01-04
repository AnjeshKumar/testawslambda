// Variables defined 

def RELEASE_BRANCH_NAME = "master"
def emailNotifications = 'anjesh.kumar@wipro.com'
def notificationSent    = false
def workspace = env.WORKSPACE

pipeline {
    //Default agent label to run all stages
    agent { label 'master'}
   
    //Default tools for agent
    tools {
        JDK 'JAVA_HOME'
        Maven 'MAVEN_HOME'
       
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
          // deleteDir()
           // Check out revision that was used to fetch the Jenkinsfile running the pipeline
          // checkout scm
          echo 'Stage 1'
        }
      }
    }

    stage('Build Lamba') {
      steps {
        script {
          bat 'java -version'
         
          echo 'Stage 2'
        }
      }
    }

  }
}
