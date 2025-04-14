pipeline {
  agent any

  tools {
    maven 'Maven 3.8.6'   // Use the exact name configured in Jenkins
    jdk 'JDK-21'          // Same here
  }
parameters {
        choice(name: 'ENVIRONMENT', choices: ['qa', 'dev', 'staging', 'prod'], description: 'Select target environment')
        choice(name: 'SIMULATION_CLASS', choices: ['simulations.apigee', 'simulations.microservice'], description: 'Fully qualified Gatling simulation class')
        string(name: 'USERS', defaultValue: '10', description: 'Number of virtual users')
            string(name: 'RAMP_DURATION', defaultValue: '10', description: 'Ramp-up duration in seconds')
            string(name: 'TEST_DURATION', defaultValue: '60', description: 'Total test duration in seconds')
    }
  environment {
      MAVEN_OPTS = '-Dmaven.repo.local=C:/Users/jibin/.m2/repository'
      GATLING_REPORTS_DIR = 'target/gatling'
    }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/Jibin-Mathew123/perfomanceAutomation.git'  // replace with your repo
      }
    }

    stage('Build & Run Gatling Test') {
      steps {
         echo "Running test on environment: ${params.ENVIRONMENT}"
          bat "mvn clean gatling:test -Dgatling.simulationClass=${params.SIMULATION_CLASS} -Denv=${params.ENVIRONMENT} -Dusers=${params.USERS} -Dramp=${params.RAMP_DURATION} -Dduration=${params.TEST_DURATION}"
      }
    }

      stage('Publish Gatling Report') {
            steps {
                def simulationName = params.SIMULATION_CLASS.split('\\.').last()
        }
        }

         post {
             always {
                 script {
                     // Read the latest run directory name from lastRun.txt
                     def lastRunFile = readFile 'target/gatling/lastRun.txt'
                     def latestRun = lastRunFile.trim()

                     echo "Latest Gatling run directory: ${latestRun}"

                     publishHTML(target: [
                         allowMissing: false,
                         alwaysLinkToLastBuild: true,
                         keepAll: true,
                         reportDir: "target/gatling/${latestRun}",
                         reportFiles: 'index.html',
                         reportName: 'Gatling Report'
                     ])
                 }
             }
      }


