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

    tools {
        nodejs "nodenv"
    }

    environment {
        S3_BUCKET = 'test-report-nagwa'
        FILE_TO_UPLOAD = 'target/*.html'
        REPO_URL = "https://dev.azure.com/nagwa-limited/Nagwa%20Classes%20Backend/_git/Api_Automation"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Assuming you're using a Git repository from Azure DevOps
                    git branch: 'main', credentialsId: 'azure_credentials_devops', url: "${REPO_URL}"
                }
            }
        }

        stage('Upload to S3') {
            steps {
                script {

                    // Upload the file to S3
                    sh "aws s3 rm s3://\"${env.S3_BUCKET}\"/ --recursive"
                    sh "aws s3 cp ${FILE_TO_UPLOAD} s3://${S3_BUCKET}/ --acl public-read --recursive"
                    echo "File uploaded to S3: s3://${S3_BUCKET}/${FILE_TO_UPLOAD}"
                }
            }
        }
    }
}
