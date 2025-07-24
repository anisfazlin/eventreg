pipeline {
    agent {
        docker {
            image 'docker:24.0.2-dind'
            args '-u root --privileged -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }


    stages {
        stage('Install Java & Maven, then Build JAR') {
            steps {
                sh '''
                apk add --no-cache openjdk17 maven
                mvn clean package -DskipTests
                '''
            }
        }


        stage('Build Docker Image') {
            steps {
                sh 'docker build -t eventreg-app .'
            }
        }


        stage('Run Docker Container') {
            steps {
                sh 'docker run -d -p 8080:8080 event-app'
            }
        }
    }
}


