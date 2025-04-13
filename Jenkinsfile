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
            script {
                // Get the latest simulation directory
                def simulationsDir = new File("${WORKSPACE}/target/gatling")
                def latestSimulation = simulationsDir
                    .listFiles()
                    .findAll { it.isDirectory() }
                    .sort { -it.lastModified() }
                    .first()

                echo "Latest Simulation Folder: ${latestSimulation}"

                // Set environment variable (optional)
                env.LATEST_GATLING_REPORT = latestSimulation.getAbsolutePath()

                // Archive the report HTML
                archiveArtifacts artifacts: "${latestSimulation}/**", fingerprint: true
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
