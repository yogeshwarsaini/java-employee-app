pipeline {
    agent any

    tools {
         maven 'Maven-3.9'
	 }

    environment {
        APP_PORT = "9090"
        JAR_NAME = "employee-app-0.0.1-SNAPSHOT.jar"
        LOG_DIR  = "/var/log/employee-app"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Code checkout...'
                git branch: 'main',
                    url: 'https://github.com/yogeshwarsaini/java-employee-app.git'
            }
        }

        stage('Build') {
            steps {
                echo '🏗️ Maven build...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running tests...'
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying app...'
                sh """
                    mkdir -p ${LOG_DIR}
                    pkill -f '${JAR_NAME}' || true
                    sleep 3
                    nohup java -jar target/${JAR_NAME} \
                        > ${LOG_DIR}/app.log 2>&1 &
                    echo "✅ App started PID: \$!"
                """
            }
        }

        stage('Health Check') {
            steps {
                echo '✔️ Health check...'
                sh """
                    sleep 15
                    curl -f http://localhost:${APP_PORT}/actuator/health || exit 1
                """
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful!'
        }
        failure {
            echo '❌ Pipeline failed!'
            sh "cat ${LOG_DIR}/app.log || true"
        }
    }
}
