pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/master']], 
                    extensions: [], 
                    userRemoteConfigs: [[
                        credentialsId: 'JenkinsCesi', 
                        url: 'https://github.com/Dialsyp/Test'
                    ]]
                ])
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
        failure {
            emailext(
                subject: "Build #${env.BUILD_NUMBER} Failed",
                body: "Le build ${env.BUILD_NUMBER} a échoué. Vérifiez les logs pour plus de détails.",
                to: 'badre.bousalem@enpc.fr',
                recipientProviders: [requestor()]
            )
        }
    }
}
