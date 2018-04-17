pipeline {
    agent { label 'docker' }
    stages {
        stage('Build') {
            steps {
                script {
                    props=readProperties file: 'gradle.properties'
                    VERSION="${props.version}-${props.apiVersion}"
                }
                sh "docker build --tag ${GIT_COMMIT} --build-arg apiVersion=${props.apiVersion} ."
            }
        }
        stage('Publish') {
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
            when { changeRequest() }
            steps {
                sh "docker tag ${GIT_COMMIT} dtr.rogfk.no/fint-beta/consumer-personal:${BRANCH_NAME}"
                withDockerRegistry([credentialsId: 'dtr-rogfk-no', url: 'https://dtr.rogfk.no']) {
                    sh "docker push 'dtr.rogfk.no/fint-beta/consumer-personal:${BRANCH_NAME}'"
                }
            }
        }
    }
}
