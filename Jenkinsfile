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

        stage('Publish to Nexus') {
            steps {
                dir('sample-java-app') {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'NEXUS_USERNAME',
                            passwordVariable: 'NEXUS_PASSWORD'
                        )
                    ]) {
                        configFileProvider([
                            configFile(
                                fileId: 'maven-settings-nexus',
                                variable: 'MAVEN_SETTINGS'
                            )
                        ]) {
                            sh '''
                                mvn -B deploy:deploy-file \
                                  -s "$MAVEN_SETTINGS" \
                                  -DgroupId=com.devops.lab7 \
                                  -DartifactId=ci-demo \
                                  -Dversion=1.0.0-SNAPSHOT \
                                  -Dpackaging=jar \
                                  -Dfile=target/ci-demo-1.0.0-SNAPSHOT.jar \
                                  -DpomFile=pom.xml \
                                  -Durl=http://nexus:8081/repository/maven-snapshots/ \
                                  -DrepositoryId=nexus
                            '''
                        }
                    }
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