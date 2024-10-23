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
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
    }
}
