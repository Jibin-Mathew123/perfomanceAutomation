pipeline {
    agent any

    tools {
        maven 'Maven 3.8.6'
        jdk 'JDK-21'
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
                    def files = findFiles(glob: 'target/gatling/*/index.html')
                    if (files.length == 0) {
                        echo "⚠️ No Gatling report found."
                        return
                    }

                    def latestReport = files.sort { -it.lastModified }.first()
                    def reportDir = new File(latestReport.path).getParent()

                    echo " Publishing Gatling report from: ${reportDir}"

                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: reportDir,
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
