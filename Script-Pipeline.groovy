node {

  stage('Repositório') { 
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/dmaiolli/api-letmewalk']]])
  }

  stage('Build') {
    withSonarQubeEnv('SonarQube') {
      sh '/opt/maven/bin/mvn clean package sonar:sonar'
    }
  }
}

stage("Quality Gate") { 
  timeout(time: 45, unit: 'MINUTES') { 
    def qualityGate = waitForQualityGate() 
      if (qualityGate.status != 'OK') {
        error "O código não está de acordo com as regras do Sonar: ${qualityGate.status}"
      }
  }
}

stage('Deploy') {		
  echo 'Realizando o Deploy em Desenvolvimento...'
  sleep 5
}