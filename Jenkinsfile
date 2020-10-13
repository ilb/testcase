pipeline {
    agent any
    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }
    stages {
        stage ('Build') {
            when {
                not {
                    expression { params.RELEASE }
                }
            }
            steps {
                dir("testcase") {
                    sh 'mvn install deploy'
                    sh 'sudo /opt/bin/tomcatmavendeploy /var/lib/tomcat-8/testcase/webapps/testcase.mavendeploy'
                }
            }
        }
        stage("Release") {
            when {
                expression { params.RELEASE }
            }
            steps {
                dir("testcase") {
                    sh "mvn -P proc -B release:prepare"
                    sh "mvn -P proc -B release:perform"
                }
            }
        }
    }
    post {
        always {
            deleteDir()
        }
    }
}