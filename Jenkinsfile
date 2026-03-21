pipeline {
    agent any

    environment {
        APP_PORT = "9090"
        JAR_NAME = "employee-app-0.0.1-SNAPSHOT.jar"
        LOG_DIR  = "/var/jenkins_home/logs/employee-app"
        EC2_HOST = "172.31.39.25"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Code checkout...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🏗️ Maven build...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying app...'
                sh """
                    mkdir -p ${LOG_DIR}

                    # JAR ko EC2 home directory mein copy karo
                    cp target/${JAR_NAME} /var/jenkins_home/logs/employee-app/

                    # EC2 pe directly run karo via docker exec host
                    pkill -f '${JAR_NAME}' || true
                    sleep 2

                    # Background mein run karo - SETSID se process independent ho jaayega
                    setsid java -jar /var/jenkins_home/logs/employee-app/${JAR_NAME} \
                        > ${LOG_DIR}/app.log 2>&1 < /dev/null &

                    echo "✅ App started with PID: \$!"
                    sleep 5
                    cat ${LOG_DIR}/app.log | tail -5
                """
            }
        }

        stage('Health Check') {
            steps {
                echo '✔️ Health check...'
                sh """
                    sleep 20
                    curl -f http://localhost:${APP_PORT}/actuator/health
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
            sh "cat ${LOG_DIR}/app.log | tail -30 || true"
        }
    }
}
