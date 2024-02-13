@Library("jenkins-agent@master") _
 
pipeline {
    agent {
        kubernetes {
        defaultContainer getDefaultContainer()
        yaml getAgentYaml()
        }
    }  

    options {
        disableConcurrentBuilds()
    }
    tools {nodejs "nodenv"}

    environment {
        REGION="us-east-1"
        CONTAINER_NAME_DEV="Api-Automation-beta"
        CONTAINER_NAME_PROD="Api-Automation-prod"
        CONTAINER_NAME_DEMO="Api-Automation-demo"
        REPO_URL = "https://nagwa-limited@dev.azure.com/nagwa-limited/Nagwa%20Classes%20Backend/_git/Api_Automation"
    }
 
    stages {
        stage('Setting-up variables') {
            steps {
                script{
                    if (env.GIT_BRANCH == 'dev'){
                        env.CONTAINER_NAME=env.CONTAINER_NAME_DEV
                    }else if (env.GIT_BRANCH == 'demo'){
                        env.CONTAINER_NAME=env.CONTAINER_NAME_DEMO
                    }else if (env.GIT_BRANCH == 'main'){
                        env.CONTAINER_NAME=env.CONTAINER_NAME_PROD
                    }else{
                        currentBuild.result = 'FAILURE'
                        error("Branch is not configured")
                    }
                    env.IMAGE_NAME="$CONTAINER_NAME-IMAGE".toLowerCase()
                    
                }
            }
        }
        
        stage('Getting Repo files') {
            steps {
                git branch: "${GIT_BRANCH}", credentialsId: 'azure_credentials_devops', url: "${REPO_URL}"
            }
        }

        stage('Build the lambda zip file') {
            steps {
                container("docker-dind"){     
                    sh '''
                    docker build -t $IMAGE_NAME:v1.0 .
                    docker create --rm --name $CONTAINER_NAME $IMAGE_NAME:v1.0 
                    docker container rm $CONTAINER_NAME
                    '''
                   
                }
            }
        }
        
    }
    
}
