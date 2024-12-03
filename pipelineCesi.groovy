pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/master']], 
                    extensions: [], 
                    userRemoteConfigs: [[
                        credentialsId: 'd6a9f5ac-5869-47bf-803b-e4708486690c', 
                        url: 'https://github.com/Dialsyp/iptv'
                    ]]
                )
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    // Exécution de tests unitaires fictifs
                    echo 'Running unit tests...'
                    def testSuccess = true // Modifie à false pour simuler un échec
                    if (!testSuccess) {
                        error "Unit tests failed!"
                    } else {
                        echo 'Unit tests passed!'
                    }
                }
            }
        }
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
    }

    post {
        success {
            emailext(
                subject: "Build #${env.BUILD_NUMBER} Succeeded",
                body: "Le build ${env.BUILD_NUMBER} a été réalisé avec succès.",
                to: 'dialsyphax@gmail.com',
                recipientProviders: [requestor()]
            )
        }
        failure {
            emailext(
                subject: "Build #${env.BUILD_NUMBER} Failed",
                body: "Le build ${env.BUILD_NUMBER} a échoué. Vérifiez les logs pour plus de détails.",
                to: 'dialsyphax@gmail.com',
                recipientProviders: [requestor()]
            )
        }
    }
}
