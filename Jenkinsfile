pipeline {
    agent { label 'agent1' }
    
    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }
    
    environment {
        APP_PORT = "9090"
        JAR_NAME = "employee-app-0.0.1-SNAPSHOT.jar"
        APP_DIR  = "/opt/employee-app"
        SERVICE_NAME = "employee-app"
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
                sh '''
                    echo "🚀 Starting Deployment..."
                    
                    # Directory create
                    sudo mkdir -p ${APP_DIR}
                    
                    # Jar copy
                    sudo cp target/${JAR_NAME} ${APP_DIR}/
                    
                    # Systemd Service File Create/Update
                    sudo bash -c "cat > /etc/systemd/system/${SERVICE_NAME}.service" << EOF
[Unit]
Description=Employee Spring Boot Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=${APP_DIR}
ExecStart=/usr/bin/java -jar ${APP_DIR}/${JAR_NAME}
Restart=always
RestartSec=10
SuccessExitStatus=143
Environment="SPRING_PROFILES_ACTIVE=prod"

[Install]
WantedBy=multi-user.target
EOF

                    # Reload systemd and restart service
                    sudo systemctl daemon-reload
                    sudo systemctl enable ${SERVICE_NAME}.service
                    sudo systemctl restart ${SERVICE_NAME}.service
                    
                    echo "✅ Service ${SERVICE_NAME} restarted successfully"
                    sudo systemctl status ${SERVICE_NAME}.service --no-pager
                '''
            }
        }
        
//         stage('Health Check') {
//     steps {
//         sh '''
//             echo "Waiting for application to start..."
//             sleep 30

//             i=1
//             while [ $i -le 12 ]; do
//                 if curl -f -s http://localhost:${APP_PORT}/actuator/health > /dev/null; then
//                     echo "✅ Application is UP and Healthy!"
//                     exit 0
//                 fi
//                 echo "Attempt $i: Application not ready yet, waiting..."
//                 i=$((i + 1))
//                 sleep 8
//             done

//             echo "❌ Health check failed after multiple attempts"
//             sudo journalctl -u ${SERVICE_NAME}.service --no-pager -n 50
//             exit 1
//         '''
//     }
// }
    }
    
    post {
        success { echo '✅ Deployment successful hai !' }
        failure { echo '❌ Pipeline failed!' }
    }
}
