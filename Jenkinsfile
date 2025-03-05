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
              sh """
               echo "Starting Sonar Scan"
               mvn sonar:sonar \
                   -Dsonar.projectKey=i27-eureka \
                   -Dsonar.host.url=http://34.16.15.150:9000 \
                   -Dsonar.login=sqa_d09b9a4eef1f4e821e420813ddee20296d1bf9cf
               """
           }
        }  
     }
}
    