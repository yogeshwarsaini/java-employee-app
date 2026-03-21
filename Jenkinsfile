pipeline {
    agent any

    environment {
        APP_PORT     = "9090"
        JAR_NAME     = "employee-app-0.0.1-SNAPSHOT.jar"
        IMAGE_NAME   = "employee-app"
        CONTAINER_NAME = "java-employee-app"
        DB_HOST      = "172.31.39.25"
        DB_PORT      = "3307"
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

        stage('Docker Build') {
            steps {
                echo '🐳 Docker image build...'
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying container...'
                sh """
                    # Purana container stop karo
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    # Naya container start karo
                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        -p ${APP_PORT}:${APP_PORT} \
                        -e SPRING_DATASOURCE_URL=jdbc:mysql://${DB_HOST}:${DB_PORT}/employeedb \
                        -e SPRING_DATASOURCE_USERNAME=root \
                        -e SPRING_DATASOURCE_PASSWORD=java123 \
                        --restart always \
                        ${IMAGE_NAME}:latest

                    echo "✅ Container started!"
                """
            }
        }

        stage('Health Check') {
            steps {
                echo '✔️ Health check...'
                sh """
                    sleep 20
                    curl -f http://${DB_HOST}:${APP_PORT}/actuator/health
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
            sh "docker logs ${CONTAINER_NAME} || true"
        }
    }
}
