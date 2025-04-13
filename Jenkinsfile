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

               // Debug: List all contents of gatling directory
               bat """
                 @echo off
                 echo Listing contents of %WORKSPACE%\\target\\gatling
                 dir /s /b "%WORKSPACE%\\target\\gatling"
               """

               // Find all simulation folders (excluding lastRun.txt)
               def simulationDirs = findFiles(glob: "${GATLING_REPORTS_DIR}/basicsimulation-*")

               if (simulationDirs.size() == 0) {
                 error "No Gatling simulation directories found matching pattern ${GATLING_REPORTS_DIR}/basicsimulation-*"
               }

               // Get the most recent simulation folder
               def latestSimulation = simulationDirs.sort { -it.lastModified }[0].name
               def reportPath = "${GATLING_REPORTS_DIR}/${latestSimulation}"

               echo " Found Gatling report at: ${reportPath}"

               // Verify index.html exists
               if (!fileExists("${reportPath}/index.html")) {
                 error " Gatling report index.html not found in ${reportPath}"
               }

               // Publish HTML report
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

               echo "✅ Successfully published Gatling report"
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
         }
         success {
           echo 'Pipeline completed successfully!'
         }
       }
     }