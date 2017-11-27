pipeline {
    agent none
    stages {
        stage('Prepare') {
            agent { label 'master' }
            steps {
                sh 'git log --oneline | nl -nln | perl -lne \'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print "$2-$1"; exit; }\' > version.txt'
                stash includes: 'version.txt', name: 'version'
            }
        }
        stage('Build') {
            agent { label 'docker' }
            steps {
                unstash 'version'
                script {
                    VERSION=readFile('version.txt').trim()
                }
                sh "docker build -t 'dtr.rogfk.no/fint-beta/consumer-personal:${VERSION}' ."
            }
        }
        stage('Publish') {
            agent { label 'docker' }
            when {
                branch 'master'
            }
            steps {
                withDockerRegistry([credentialsId: 'dtr-rogfk-no', url: 'https://dtr.rogfk.no']) {
                    unstash 'version'
                    script {
                        VERSION=readFile('version.txt').trim()
                    }
                    sh "docker push 'dtr.rogfk.no/fint-beta/consumer-personal:${VERSION}'"
                }
            }
        }
    }
}
