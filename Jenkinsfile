pipeline {
    agent any
    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }
        stage('Build & Execute') {
            steps {
                // Compiles and runs the default system outputs via Maven
                sh 'mvn clean compile exec:java -Dexec.mainClass="com.devops.assessment.App"'
            }
        }
    }
}
