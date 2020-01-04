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
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }
  
  stages {

    stage('Stage 1') {
      steps {
        script {
          echo 'Stage 1'
        }
      }
    }

    stage('Stage 2') {
      steps {
        script {
          echo 'Stage 2'
        }
      }
    }

  }
}
