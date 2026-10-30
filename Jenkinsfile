pipeline {
    agent any

    tools {
        maven 'M3'
    }

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('sample-java-app') {
                    sh 'mvn -B clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                dir('sample-java-app') {
                    sh 'mvn -B test'
                }
            }
            post {
                always {
                    junit 'sample-java-app/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package and Archive') {
            steps {
                dir('sample-java-app') {
                    sh 'mvn -B package -DskipTests'
                    archiveArtifacts(
                        artifacts: 'target/*.jar',
                        fingerprint: true
                    )
                }
            }
        }
    }

    post {
        success {
            echo 'PIPELINE SUCCESS!'
        }
        failure {
            echo 'PIPELINE FAILED - Check Console Output'
        }
    }
}
