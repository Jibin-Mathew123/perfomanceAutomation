pipeline {
  agent any

  tools {
    maven 'Maven 3.8.6'   // Use the exact name configured in Jenkins
    jdk 'JDK-21'          // Same here
  }
parameters {
        choice(name: 'ENVIRONMENT', choices: ['qa', 'dev', 'staging', 'prod'], description: 'Select target environment')
        choice(name: 'SIMULATION_CLASS', choices: ['simulations.apigee', 'simulations.microservice'], description: 'Fully qualified Gatling simulation class')
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
                            bat "mvn clean gatling:test -Dgatling.simulationClass=${params.SIMULATION_CLASS} -Denv=${params.ENVIRONMENT}"
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
                def targetPath = "${params.SIMULATION_CLASS}".split(".")[1]

                // Find the most recent simulation directory
                def cmd = """
                  @echo off
                  for /f "tokens=*" %%i in ('dir /ad /b /od "${gatlingPath}\\"${targetPath}"-*"') do (
                    set LAST_DIR=%%i
                  )
                  echo %LAST_DIR%
                """

                def lastDir = bat(script: cmd, returnStdout: true).trim()

                if (!lastDir) {
                  error " No Gatling simulation directories found in ${gatlingPath}"
                }

                def reportPath = "${env.GATLING_REPORTS_DIR}/${lastDir}".replace('\\', '/')
                echo "Found Gatling report at: ${reportPath}"

               // Create a self-contained version of the report
                     bat """
                       @echo off
                       set "reportPath=${reportPath}"

                       :: Create a temporary directory
                       mkdir "%reportPath%\\jenkins-report"

                       :: Copy all required files
                       copy "%reportPath%\\index.html" "%reportPath%\\jenkins-report\\"
                       copy "%reportPath%\\js\\*" "%reportPath%\\jenkins-report\\"
                       copy "%reportPath%\\style\\*" "%reportPath%\\jenkins-report\\"

                       :: Modify index.html to use correct paths
                       powershell -Command "(Get-Content '%reportPath%\\index.html') -replace 'js/', '' -replace 'style/', '' | Set-Content '%reportPath%\\jenkins-report\\index.html'"
                     """

                     publishHTML([
                       target: [
                         reportDir: "${reportPath}/jenkins-report",
                         reportFiles: 'index.html',
                         reportName: 'Gatling Report',
                         keepAll: true,
                         alwaysLinkToLastBuild: true
                       ]
                     ])

                echo "Successfully published Gatling report"

                // Store the report path for post-build actions
                env.REPORT_PATH = reportPath
              }
            }
          }
        }

        post {
          always {
            script {
              // Use the stored report path or fallback to default
              def archivePath = env.REPORT_PATH ?: 'target/gatling'
              archiveArtifacts artifacts: "${archivePath}/**/*", allowEmptyArchive: true
            }
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


