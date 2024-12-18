pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/master']], 
                    extensions: [
                        [$class: 'CloneOption', shallow: true, depth: 1]
                    ], 

                    userRemoteConfigs: [[
                        credentialsId: 'd6a9f5ac-5869-47bf-803b-e4708486690c', 
                        url: 'https://github.com/Dialsyp/tensorflow.git'
                    ]]
                )
            }
        }
        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--format XML', nvdCredentialsId: 'NVDKey', odcInstallation: 'DependencyCheck'
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }
    }

    //post {
    //    success {
     //       emailext(
     //           subject: "Build #${env.BUILD_NUMBER} Succeeded",
     //           body: "Le build ${env.BUILD_NUMBER} a été réalisé avec succès.",
     //           to: 'dialsyphax@gmail.com',
     //           recipientProviders: [requestor()]
     //       )
     //   }
     //   failure {
     //       emailext(
     //           subject: "Build #${env.BUILD_NUMBER} Failed",
     //           body: "Le build ${env.BUILD_NUMBER} a échoué. Vérifiez les logs pour plus de détails.",
     //           to: 'dialsyphax@gmail.com'
     //           recipientProviders: [requestor()]
      //      )
      //  }
   // }
}
