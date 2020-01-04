
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
        
    
    options {
        //Disable concurrentbuilds for the same job
        disableConcurrentBuilds()
        // Discard previos builds
        buildDiscarder(logRotator(numToKeepStr: env.BRANCH_NAME.equals(RELEASE_BRANCH_NAME) ? '5' : '2')) 

        // Colorize the console log
        ansiColor("xterm")        
      
        
        // Add timestamps to console log
        timestamps()
        
    }
    triggers {
            github(
                triggerOnPush: true,
                triggerOnMergeRequest: true,
                branchFilterType: 'All',
                addVoteOnMergeRequest: true)
        }
        
    stages {
        stage('SCM Checkout') {
            steps {
                githubCommitStatus(name: 'SCM Checkout') {
                    // Run groovy script
                    script {
                        // Make sure we're in a clean space
                     	deleteDir()
                        // Check out revision that was used to fetch the Jenkinsfile running the pipeline
                        scm
                     }
                 } 
             }
          }
          
          
          stage('Maven Build') {
            steps {
                gitlabCommitStatus(name: 'Maven Build') {
                    // Run groovy script
                    script {
                    	withMaven() {
                            sh "mvn clean compile"
                        }
                       
                     }
                 }              
             
             }
          }
          
         
                    
         
          
         
          
      }
      
      
      
      post {
       
        failure {
            echo 'I failed :('
            // notify users when they check into a broken build
            sendNotification buildChanged:false
            
        }
        changed {
            echo 'Things were different before...'
            sendNotification buildChanged:true
        }
   
    }
  } 
  
  def sendNotification(buildChanged) {
  
   
    notificationSent = true
    
    if (currentBuild.currentResult == 'SUCCESS')
    { 
        // notify users when the build is back to normal
        mail to: 'anjesh.kumar@wipro.com',
            subject: "Build fixed: ${currentBuild.fullDisplayName}",
            body: "The build is back to normal ${env.BUILD_URL}"
            
    }
    else if ((currentBuild.currentResult == 'FAILURE') && buildChanged)
    {
        // notify users when the Pipeline first fails
        mail to: 'anjesh.kumar@wipro.com',
            subject: "Build failed: ${currentBuild.fullDisplayName}",
            body: "Something went wrong with ${env.BUILD_URL}"
           
    }
    else if ((currentBuild.currentResult == 'FAILURE'))
    {
        // notify users when they check into a broken build
        mail to: 'anjesh.kumar@wipro.com',
            subject: "Build failed (again): ${currentBuild.fullDisplayName}",
            body: "Something is still wrong with ${env.BUILD_URL}"
            
    }
} 
