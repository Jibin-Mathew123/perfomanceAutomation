pipeline {
  agent any

  tools {
    maven 'Maven 3.8.6'   // Use the exact name configured in Jenkins
    jdk 'JDK-21'          // Same here
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
        bat 'mvn clean gatling:test'
      }
    }

     stage('Archive Gatling Reports') {
          steps {
            script {
              // Wait for reports to be generated
              sleep(time: 10, unit: 'SECONDS')

              // Find the most recent simulation folder
              def simulationDirs = findFiles(glob: "${GATLING_REPORTS_DIR}/*")

              if (simulationDirs.isEmpty()) {
                error "No Gatling simulation directories found in ${GATLING_REPORTS_DIR}"
              }

              def latestSimulation = simulationDirs.sort { -it.lastModified }.first().name
              def reportPath = "${GATLING_REPORTS_DIR}/${latestSimulation}"

              echo "Publishing Gatling report from: ${reportPath}"

              // Verify report files exist
              if (!fileExists("${reportPath}/index.html")) {
                error "Gatling report index.html not found in ${reportPath}"
              }

              publishHTML([
                target: [
                  reportDir: reportPath,
                  reportFiles: 'index.html',
                  reportName: 'Gatling Report',
                  keepAll: true,
                  alwaysLinkToLastBuild: true,
                  allowMissing: false
                ]
              ])
            }
          }
        }
      }

      post {
        always {
          archiveArtifacts artifacts: "${GATLING_REPORTS_DIR}/**/*", allowEmptyArchive: true
          echo 'Pipeline execution finished.'
        }
        failure {
          echo 'There was a failure. Check the logs and report.'
          // Optional: Send notification
          // emailext body: 'Build failed!', subject: 'Build Failed', to: 'your@email.com'
        }
        success {
          echo 'Pipeline completed successfully!'
        }
      }
    }