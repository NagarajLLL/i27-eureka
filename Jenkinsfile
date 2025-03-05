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
         POM_PACKAGING = readMavenPom().getpackaging()
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
         //        sh 'mvn test'
         //     }
         //  }

          stage ('SonarQube'){
            steps {
               sh """
               echo "Starting Sonar Scan"
               mvn sonar:sonar \
                   -Dsonar.projectKey=i27-eureka \
                   -Dsonar.host.url=http://34.16.15.150:9000 \
                   -Dsonar.login=sqa_a42c2667f41403658dddf268942357a60e963349
               """    
            }
          } 
          stage ('BuildFormat'){
              steps {
                script { // i27-eureka-0.0.1-SNAPSHOT.jar
                sh """
                 echo "Testing JAR Source: i27-${env.APPLICATION_NAME}.${env.POM_VERSION}.${env.POM_PACKAGING}"
                """

              }
          }
     }

    