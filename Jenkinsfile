
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
    
    environment {      
      http_proxy = 'proxy2.wipro.com:8080'
      https_proxy = 'proxy2.wipro.com:8080'
    }

    
    options {
        //Disable concurrentbuilds for the same job
        disableConcurrentBuilds()
        // Discard previos builds
        buildDiscarder(logRotator(numToKeepStr: env.BRANCH_NAME.equals(RELEASE_BRANCH_NAME) ? '5' : '2')) 

        // Colorize the console log
        ansiColor("xterm")
        
        // Tell GitLab about the build stages we're about to run
        gitLabConnection('AN366419_WiproGitlab')
        gitlabBuilds(builds: ['SCM Checkout', 'Maven Build'])
        // Add timestamps to console log
        timestamps()
        
    }
    triggers {
            gitlab(
                triggerOnPush: true,
                triggerOnMergeRequest: true,
                branchFilterType: 'All',
                addVoteOnMergeRequest: true)
        }
        
    stages {
        stage('SCM Checkout') {
            steps {
                gitlabCommitStatus(name: 'SCM Checkout') {
                    // Run groovy script
                    script {
                        // Make sure we're in a clean space
                     	deleteDir()
                        // Check out revision that was used to fetch the Jenkinsfile running the pipeline
                        //checkout scm
                        git credentialsId: 'AN366419_WiproGitlab', url: 'https://topgear-training-gitlab.wipro.com/AN366419/DevOpsProfessional_Batch6_Capstone_OnlineAddressBook.git'
                        
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
          
          stage('Analysis Code Quality') {
            steps {
                    // Run groovy script
                    script {
                    	withSonarQubeEnv('SonarQube_AN366419') { 
                            sh "mvn test sonar:sonar"
                        }
                       
                     }
             
             }
          }
          
          
          
          stage('Integration Tests') {
            steps {
                    // Run groovy script
                    script {
                    	withMaven() {
                            sh "mvn install"
                        }
                       
                     }
             
             }
          }
          
          stage('Artifacts Upload') {
            steps {
                    // Run groovy script
                    script {
                    	def server = Artifactory.server "AN366419_Artifactory"
                        def buildInfo = Artifactory.newBuildInfo()
                        buildInfo.env.capture = true
                        buildInfo.env.collect()

                        def uploadSpec = """{
                        "files": [
                            {
                            "pattern": "${pwd()}/target/*.jar",
                            "target": "libs-snapshot-local"
                          }, {
                            "pattern": "${pwd()}/target/*.pom",
                            "target": "libs-snapshot-local"
                          }, {
                            "pattern": "${pwd()}/target/*.war",
                            "target": "libs-snapshot-local"
                          }
                        ]
                      }"""
                      // Upload to Artifactory.
                      server.upload(uploadSpec)
                      //server.upload spec: uploadSpec, buildInfo: buildInfo
                
                      buildInfo.retention maxBuilds: 10, maxDays: 7, deleteBuildArtifacts: true
                      // Publish build info.
                      server.publishBuildInfo buildInfo
                       
                     }
                             
             
             }
          }
          
          stage('Dev Test Environment') {
            steps {
                    // Run groovy script
                    script {
                    	ansiColor('xterm') {
                        ansiblePlaybook(
                        playbook: '/home/osgdev/ansilab/roles/AN366419_Tomcat_Deploy/AN366419_Tomcat_Deploy.yml',
                        inventory: '/home/osgdev/ansilab/ansiserver',
                        credentialsId: 'Ansible_Login_Key',
                        extras : "-e ansible_sudo_pass=osg@1234",
                            colorized: true)
                    	}	
                       
                     }
                            
             
             }
          }
          // Only want to deploy from master branch,
          stage('Production Release') {
           
            steps {
               
                    // Run groovy script
                    script {
                        // Randomly Proxy authentication 
                    	//withDockerRegistry([ credentialsId: "DockerHubCredentials", url: "" ]) {
                            //clear existing docker build image
                            sh 'docker container stop addreddbook'
                            sh 'docker container rm addreddbook'
                            sh 'docker image rmi -f onlineaddressbook:1.0'
                            //docker build image
                            sh 'docker build -t onlineaddressbook:1.0 .'
                            // following commands will be executed within logged docker registry
                            //sh 'docker push onlineaddressbook:1.0'
                            
                            
                            sh 'docker run -d --name  addreddbook -p8081:8080 onlineaddressbook:1.0' 
                       // }
                       
                    
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
