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
        FOLDER_TO_UPLOAD = 'target/Reports'
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
                    sh """
                        cd ${FOLDER_TO_UPLOAD}"
                        for file in *; do
                        # Check if the file is 'TestReport.html' with a leading space
                        if [[ "\$file" == " TestReport.html" ]]; then
                            mv "\$file" "\${file## }"  # This removes the leading space
                        fi
                        done
                    """
                    // Upload the file to S3
                    sh "aws s3 rm s3://\"${env.S3_BUCKET}\"/ --recursive"
                    sh "aws s3 cp ${FOLDER_TO_UPLOAD} s3://${S3_BUCKET}/ --acl public-read --recursive"
                    echo "File uploaded to S3: s3://${S3_BUCKET}/${FOLDER_TO_UPLOAD}"
                }
            }
        }
    }
}
