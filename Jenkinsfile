pipeline {
    agent any

    tools {
        maven 'Maven-3.8.6'
        jdk 'JDK-17'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=C:/Users/jibin/.m2/repository'
    }

    stages {
        stage('Build & Run Gatling') {
            steps {
                bat 'mvn clean gatling:test'
            }
        }

        stage('Publish Gatling Report') {
            steps {
                script {
                    def baseDir = new File("${env.WORKSPACE}/target/gatling")
                    def latestReport = baseDir
                        .listFiles()
                        .findAll { it.isDirectory() }
                        .sort { -it.lastModified() }
                        .first()

                    echo "Latest Gatling report folder: ${latestReport}"

                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: "${latestReport}",
                        reportFiles: 'index.html',
                        reportName: 'Gatling Report'
                    ])
                }
            }
        }
    }

    post {
        failure {
            echo "❌ Build failed. Check Maven or Gatling errors."
        }
        success {
            echo "✅ Pipeline finished successfully!"
        }
    }
}
