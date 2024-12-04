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
                sh '''
                if [ ! -d "dependency-check-report/html" ]; then
                    mkdir -p dependency-check-report/html
                fi
                '''
            }
        }

        stage('OWASP Dependency-Check') {
            steps {
                dependencyCheck additionalArguments: '', 
                               nvdCredentialsId: 'NVDKey', // Assurez-vous que cet ID correspond à votre configuration d'identifiants
                               odcInstallation: 'DependencyCheck' // Assurez-vous que ce nom correspond à votre configuration
            }
        }

        stage('Publish Dependency Check Reports') {
            steps {
                // Publier les rapports HTML et JSON
                publishHTML(target: [
                    reportName: 'Dependency Check Report',
                    reportDir: 'dependency-check-report/html', // Chemin vers le rapport HTML
                    reportFiles: 'index.html', // Fichier du rapport HTML à publier
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    allowMissing: false
                ])
                
                // Archive les fichiers JSON pour référence future
                archiveArtifacts artifacts: 'dependency-check-report/*.json', allowEmptyArchive: true
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
