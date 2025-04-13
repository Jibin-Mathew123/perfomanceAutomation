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

      stage('Publish Gatling Report') {
            steps {
              script {
                // Wait for files to be written
                sleep(time: 15, unit: 'SECONDS')

                // Debug: Show exact directory structure
                bat """
                  @echo off
                  echo FULL GATLING DIRECTORY STRUCTURE:
                  dir /s /b "%WORKSPACE%\\target\\gatling"
                  echo CURRENT WORKSPACE: %WORKSPACE%
                """

                // Get the actual path that works
                def gatlingPath = "${env.WORKSPACE}\\target\\gatling".replace('/', '\\')

                // Find the most recent simulation directory
                def cmd = """
                  @echo off
                  for /f "tokens=*" %%i in ('dir /ad /b /od "${gatlingPath}\\basicsimulation-*"') do (
                    set LAST_DIR=%%i
                  )
                  echo %LAST_DIR%
                """

                def lastDir = bat(script: cmd, returnStdout: true).trim()

                if (!lastDir) {
                  error " No Gatling simulation directories found in ${gatlingPath}"
                }

                def reportPath = "${GATLING_REPORTS_BASE}/${lastDir}".replace('\\', '/')
                echo " Found Gatling report at: ${reportPath}"

                // Publish the report
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
            archiveArtifacts artifacts: "${GATLING_REPORTS_BASE}/**/*", allowEmptyArchive: true
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