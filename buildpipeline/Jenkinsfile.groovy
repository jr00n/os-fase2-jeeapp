#!groovy
def mvnCmd = "mvn -s buildpipeline/maven-settings.xml"
def appName = "os-fase2-jeeapp"
def version
def project = env.PROJECT_NAME
def testSonarHostUrl = 'https://ooossrql11.ont.belastingdienst.nl/sonar/'
def testSonarLoginId = '0d75d7f2e108b6171cac29522e20fea444933be8'

pipeline {
    agent none
    options {
		skipDefaultCheckout()
	}
    stages {
        stage('Maven build & unit test') {
            agent {
                label "jos-m3-openjdk8"
            }
            steps {
                checkout scm
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    version = pom.version
                }
		    	sh(script: "${mvnCmd} clean package")
		    	// maven profile 'sonarqube' staat in root pom JOS
		    	//sh(script: "${mvnCmd} sonar:sonar -Psonarqube -Dsonar.host.url=${testSonarHostUrl} -Dsonar.login=${testSonarLoginId} ")
            }
		}

        /*
        stages {
            stage('Build image') {
                steps {
                    script {
                        openshift.withCluster() {
                            openshift.withProject(env.INVENTORY_DEV_PROJECT) {
                                openshift.startBuild("inventory", "--wait=true")
                            }
                        }
                    }
                }
            }
            stage('Run Tests in DEV') {
                steps {
                    sleep 10
                }
            }
        */
    }
}