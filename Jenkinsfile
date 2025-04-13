pipeline {
  agent any

  tools {
    maven 'Maven 3.8.6'   // Use the exact name configured in Jenkins
    jdk 'JDK-21'          // Same here
  }

  environment {
      MAVEN_OPTS = '-Dmaven.repo.local=C:/Users/jibin/.m2/repository'
    }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/Jibin-Mathew123/perfomanceAutomation.git'  // replace with your repo
      }
    }

    stage('Build & Run Gatling Test') {
      steps {
        bat 'mvn clean gatling:test'
      }
    }

    stage('Archive Gatling Reports') {
      steps {
        publishHTML(target: [
          reportDir: 'target/gatling/basicsimulation/*',
          reportFiles: 'index.html',
          reportName: 'Gatling Report',
          keepAll: true
        ])
      }
    }
  }

  post {
    always {
      echo 'Pipeline execution finished.'
    }
    failure {
      echo 'There was a failure. Check the logs and report.'
    }
  }
}
