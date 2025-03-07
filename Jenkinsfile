// Thsi Jenkins file is for Eureka Deployment

pipeline {
     agent {
          label 'k8s-slave'
     }

     // tools configured in jenkins-master
     tools {
        maven 'Maven-3.8.8'
        jdk 'JDK-17'
     }
     environment {
         APPLICATION_NAME = "eureka"
         POM_VERSION = readMavenPom().getVersion()
         POM_PACKAGING = readMavenPom().getPackaging()
     }

     stages {
          stage ('build'){
             // This is where Build for Eureka application happens
             steps {
                 echo "Building ${env.APPLICATION_NAME} Application"
                 sh 'mvn clean package -DskipTest=true'
                 //mvn clean package -DskipTests=true
                 //mvn clean package -Dmaven.test.skip=true
                 archive 'target/*.jar'
             }
          }
         //  stage ('Unit Test') {
         //     steps {
         //        echo "**** Performing Unit test for ${env.APPLICATION_NAME} Application ***"
         //        sh 'mvn test'  test
         //     }
         //  }

        stage ('SonarQube'){
           steps {
               //COde Quality needs to be implemented in this stage
               //Before we execute or write the code, make suer sonarqube-sanner plugin is installed 
               // sonar detaails are been configured in the manage jenkins > system
               echo "*******Starting Sonar Scans with Quality Gates*********"
               withSonarQubeEnv('SonarQube') {// SonarQube is the name we configured in Manage Jenkins > system > Sonarqube , it hsould match exactly
                   sh """
                      mvn sonar:sonar \
                          -Dsonar.projectKey=i27-eureka \
                          -Dsonar.host.url=http://34.16.15.150:9000 \
                          -Dsonar.login=sqa_d09b9a4eef1f4e821e420813ddee20296d1bf9cf
               """
               }
               timeout (time: 2, unit: 'MINUTES') {
                   waitForQualityGate abortPipeline: true
               }
             
           }
        } 
        stage ('BuildFormat') {
            steps {
               script { 
                   // Existing : i27-eureka-0.0.1-SNAPSHOT.jar
                   // Destination : i27-eureka-buildnumber-branchname.package
                 sh """
                   echo "Testing JAR Source: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                   echo "Testing JAr Destination Format: i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                 """
               }
            }
        }
        stage('Docker build') {
            steps {
                script{
                    echo "****** Building Doker image *******"
                    sh "cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd"
                    sh "docker build --no-cache --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t sampleeureka:v1 ./.cicd/"
                }
            }
        }
       
     }
}
    