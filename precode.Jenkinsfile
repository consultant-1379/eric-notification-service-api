#!/usr/bin/env groovy

def bob = "./bob/bob -r ./ruleset2.0.yaml"
pipeline {
    options {
        disableConcurrentBuilds()
    }
    agent {
        label env.NODE_LABEL
    }
    stages {
        stage('Inject Credential Files') {
            steps {
                withCredentials([file(credentialsId: 'so-secret-settings-file', variable: 'settingsFile')]) {
                    sh 'install -m 600 "$settingsFile" settings.xml'
                }
            }
        }
        stage('Clean Workspace') {
            steps {
                sh 'echo sh "${bob} clean"'
                sh 'git clean -xdff --exclude=.m2 --exclude=settings.xml'
            }
        }
        stage('Jar compile') {
            steps {
               sh "${bob} compile"
            }
        }
        stage('Unit tests') {
            steps {
                sh "${bob} test"
            }
        }
    }
    post {
        success {
            script {
                sh "ssh -p 29418 gerrit.ericsson.se gerrit review \${GIT_COMMIT} --label Verified=1"
            }
        }
        failure {
            script {
                sh "ssh -p 29418 gerrit.ericsson.se gerrit review \${GIT_COMMIT} --label Verified=-1"
            }
        }
    }
}