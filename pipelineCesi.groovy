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
         stage('Préparer le dossier') {
            steps {
                // Vérifier si le dossier existe, sinon le créer
                sh 'rm -rf /var/lib/jenkins/workspace/Analyse\\ Code\\ Cesi/*'
                sh '''
                if [ ! -d "dependencycheckreport/html" ]; then
                    mkdir -p dependencycheckreport/html
                fi
                '''
            }
        }

        stage('Nettoyer les anciens rapports') {
            steps {
                // Supprimer les anciens fichiers dans le répertoire de rapports
                sh 'rm -rf dependencycheckreport/html/*'
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '', nvdCredentialsId: 'NVDKey', odcInstallation: 'DependencyCheck'
            }
        }
        stage('Publish OWASP Dependency Check Report') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: './dependencycheckreport/html',
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
