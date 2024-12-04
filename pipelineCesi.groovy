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
                        url: 'https://github.com/Amora-Corporation/monolithApplication'
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
    //     stage('SonarCloud Analysis') {
    //        steps {
    //            script {
    //                def scannerHome = tool 'SonarQubeTool' // Assurez-vous que l'outil est configuré dans Jenkins
    //                withSonarQubeEnv('SonarQube') { // Remplacez par le nom que vous avez donné à votre serveur SonarCloud
    //                    sh "${scannerHome}/bin/sonar-scanner"
    //                }
    //            }
    //        }
    //    }

stage('Code Style Analysis') {
    steps {
        sh 'npx eslint . --ext .js,.ts'
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
