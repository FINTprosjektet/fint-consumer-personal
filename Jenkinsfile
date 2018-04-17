pipeline {
    agent none
    stages {
        stage('Build') {
            agent { label 'docker' }
            steps {
                script {
                    props=readProperties file: 'gradle.properties'
                    VERSION="${props.version}-${props.apiVersion}"
                }
                sh "docker build --tag ${GIT_COMMIT} --build-arg apiVersion=${props.apiVersion} ."
            }
        }
        stage('Publish') {
            agent { label 'docker' }
            when {
                branch 'master'
            }
            steps {
                sh "docker tag ${GIT_COMMIT} dtr.rogfk.no/fint-beta/consumer-personal:${VERSION}"
                withDockerRegistry([credentialsId: 'dtr-rogfk-no', url: 'https://dtr.rogfk.no']) {
                    sh "docker push 'dtr.rogfk.no/fint-beta/consumer-personal:${VERSION}'"
                }
            }
        }
        stage('Publish PR') {
            agent { label 'docker' }
            when { changeRequest() }
            steps {
                sh "docker tag ${GIT_COMMIT} dtr.rogfk.no/fint-beta/consumer-personal:${CHANGE_ID}"
                withDockerRegistry([credentialsId: 'dtr-rogfk-no', url: 'https://dtr.rogfk.no']) {
                    sh "docker push 'dtr.rogfk.no/fint-beta/consumer-personal:${CHANGE_ID}'"
                }
            }
        }
    }
}
