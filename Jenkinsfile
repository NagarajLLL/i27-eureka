pipeline {
    agent {
        label 'k8s-slave'
    }

    tools {
        maven 'Maven-3.8.8'
        jdk 'JDK-17'
    }

    environment {
        APPLICATION_NAME = "eureka"
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
        DOCKER_HUB = "docker.io/bslcnr"
        DOCKER_CREDS = credentials('dockerhub_creds') 
    }

    stages {
        stage('build') {
            steps {
                echo "Building ${env.APPLICATION_NAME} Application"
                sh 'mvn clean package -DskipTest=true'
                archive 'target/*.jar'
            }
        }

        stage('SonarQube') {
            steps {
                echo "*******Starting Sonar Scans with Quality Gates*********"
                withSonarQubeEnv('SonarQube') {
                    sh """
                    mvn sonar:sonar \
                        -Dsonar.projectKey=i27-eureka \
                        -Dsonar.host.url=http://34.16.15.150:9000 \
                        -Dsonar.login=sqa_d09b9a4eef1f4e821e420813ddee20296d1bf9cf
                    """
                }
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('BuildFormat') {
            steps {
                script {
                    sh """
                    echo "Testing JAR Source: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                    echo "Testing JAr Destination Format: i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                    """
                }
            }
        }

        stage('Docker build & push') {
            steps {
                script {
                    dockerBuildAndPush().call()
                }
            }
        }

        stage('Deploy to Dev Env') {
            steps {
                script {
                    dockerDeploy('dev', '5761', '8761').call()
                }
            }
        }

        stage('Deploy to Test Env') {
            steps {
                script {
                    dockerDeploy('tst', '6761', '8761').call()
                }
            }
        }

        stage('Deploy to Stage Env') {
            steps {
                script {
                    dockerDeploy('stg', '7761', '8761').call()
                }
            }
        }

        stage('Deploy to Prod Env') {
            steps {
                script {
                    dockerDeploy('prod', '8761', '8761').call()
                }
            }
        }
    }

    // Method for Docker build and Push
    def dockerBuildAndPush() {
        return }
            echo "****** Building Docker image *******"
            sh "cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd"
            sh "docker build --no-cache --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd/"
            sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
            echo "********* Push Image to Docker registry ***********"
            sh "docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
        }
    

    // Method for Docker Deployment as containers in different env's
    def dockerDeploy(envDeploy, hostPort, contPort) {
        return {
            echo "********* Deploying to $envDeploy Environment **************"
            withCredentials([usernamePassword(credentialsId: 'john_docker_vm_passwd', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                script {
                    try {
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$dev_ip \"docker stop ${env.APPLICATION_NAME}-$envDeploy\""
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$dev_ip \"docker rm ${env.APPLICATION_NAME}-$envDeploy\""
                    } catch (err) {
                        echo "Error Caught: $err"
                    }
                    sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$dev_ip \"docker container run -dit -p $hostPort:$contPort --name ${env.APPLICATION_NAME}-$envDeploy ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}\""
                }
            }
        }
    }
