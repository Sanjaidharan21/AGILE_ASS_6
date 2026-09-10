pipeline {
    agent any
    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }
        stage('Execute Question 1') {
            steps {
                echo 'Running Employee Access Eligibility Assessment Pipeline...'
                sh 'mvn clean compile exec:java -Dexec.mainClass="com.devops.assessment.EmployeeAccessSystem"'
            }
        }
        stage('Execute Question 2') {
            steps {
                echo 'Running Smart Loan Evaluation Pipeline...'
                sh 'mvn exec:java -Dexec.mainClass="com.devops.assessment.SmartLoanSystem"'
            }
        }
    }
}
