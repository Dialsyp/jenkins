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
                dependencyCheck additionalArguments: '--out ./reports/dependency-check', nvdCredentialsId: 'NVDKey', odcInstallation: 'DependencyCheck'
            }
        }
        stage('Publish OWASP Dependency Check Report') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: './reports/dependency-check',
                    reportFiles: 'dependency-check-report.html',
                    reportName: 'OWASP Dependency Check Report'
                ])
            }
        }

        
        //stage('SonarCloud Analysis') {
        //    steps {
        //        script {
        //            def scannerHome = tool 'SonarQubeTool' // Assurez-vous que l'outil est configuré dans Jenkins
        //            withSonarQubeEnv('SonarQube') { // Remplacez par le nom que vous avez donné à votre serveur SonarCloud
        //                sh "${scannerHome}/bin/sonar-scanner"
        //            }
        //        }
        //    }
       // }
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
     //           to: 'dialsyphax@gmail.com',
     //           recipientProviders: [requestor()]
      //      )
      //  }
   // }
}
