pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        APP_PORT     = "9090"
        JAR_NAME     = "employee-app-0.0.1-SNAPSHOT.jar"
        APP_DIR      = "/opt/employee-app"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    sudo mkdir -p ${APP_DIR}
                    sudo cp target/${JAR_NAME} ${APP_DIR}/

                    # Purana process kill karo
                    sudo pkill -f '${JAR_NAME}' || true
                    sleep 3

                    # Naya process start karo as systemd service
                    sudo systemctl restart employee-app || true
                """
            }
        }

        stage('Health Check') {
            steps {
                sh """
                    sleep 20
                    curl -f http://localhost:${APP_PORT}/actuator/health
                """
            }
        }
    }

    post {
        success { echo '✅ Deployment successful hai !' }
        failure { echo '❌ Pipeline failed!' }
    }
}
