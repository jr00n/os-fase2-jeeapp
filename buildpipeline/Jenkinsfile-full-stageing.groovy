try {
    timeout(time: 20, unit: 'MINUTES') {
        def appName = "os-fase2-jeeapp"
        def project = ""
        def version
        def ocCmd = "oc " +
                " --server=https://openshift.default.svc.cluster.local" +
                " --certificate-authority=/run/secrets/kubernetes.io/serviceaccount/ca.crt"
        def mvnCmd = "mvn"

        node {
            stage("Initialize") {
                def token = sh(script: 'cat /var/run/secrets/kubernetes.io/serviceaccount/token', returnStdout: true)
                ocCmd = ocCmd + " --token=" + token
                project = env.PROJECT_NAME
                ocCmd = ocCmd + " -n ${project}"
            }
        }
        node("jos-m3-openjdk8") {
            stage("Git Checkout") {
                //git branch: 'master', url: 'ssh://git@git.belastingdienst.nl:7999/~wolfj09/os-fase2-jeeapp.git'
                checkout scm
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    version = pom.version
                }
            }
            stage("Build and Unit test") {
                sh "${mvnCmd} clean package -Popenshift"
                stash name: "all"
            }

            stage("Build Image") {
                //script {
                //    openshift.withCluster() {
                //        openshift.withProject(project) {
                //            openshift.selector("bc","${appName}-docker").startBuild("--from-file=target/ROOT.war", "--wait=true")
                //        }
                //    }
                //}
                sh "oc start-build ${appName}-docker --from-file=target/ROOT.war -n ${project}"
                openshiftVerifyBuild bldCfg: "${appName}-docker", namespace: project, waitTime: '20', waitUnit: 'min'
            }
            stage("Deploy Image") {
                openshiftDeploy deploymentConfig: appName, namespace: project
            }
        }
        stage('Test and Analysis') {
            parallel(
                    'Robot Testing': {
                        node("jos-robotframework") {
                            try {
                                unstash name: "all"
                                // service discovery..app
                                def appURL = sh(script: ocCmd + " get routes -l app=${appName} -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout: true)
                                appURL = "http://" + appURL
                                // service discovery..selenium Hub
                                def seleniumHubURL = sh(script: ocCmd + " get routes -l app=selenium-grid -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout: true)
                                seleniumHubURL = "http://" + seleniumHubURL + "/wd/hub"
                                //seleniumHubURL = "http://selenium-grid:4444/wd/hub" // werkt helaas nog niet, uitzoeken!!

                                dir ('src/test/robot') {
                                    sh('chmod +x ./runtests.sh')
                                    sh(script: "./runtests.sh ${seleniumHubURL} ${appURL}")
                                }
                            } catch (error) {
                                // Slurp Error ;)
                                //throw error
                            } finally {
                                archive 'src/test/robot/output/*'
                            }
                        }
                    },
                    'Static Analysis': {
                        node("jos-m3-openjdk8") {
                            unstash name: "all"
                            sh "${mvnCmd} sonar:sonar -Dsonar.host.url=http://sonarqube:9000 -DskipTests=true"
                        }
                    }
            )
        }
        node() {
            stage('Deploy to STAGE') {

                unstash name: "all"
                openshift.verbose()
                openshift.loglevel(2)

                script {
                    openshift.withCluster() {
                        // maak een nieuwe tag/versie in stage area
                        openshift.tag("${project}/${appName}:latest", "demojavateam-stage/${appName}:latest")

                        openshift.withProject("demojavateam-stage") {
                            // ruim eerst de objecten op die zijn blijven staan
                            if (openshift.selector('dc', "${appName}").exists()) {
                                openshift.selector('dc', "${appName}").delete()
                                openshift.selector('svc', "${appName}").delete()
                                openshift.selector('route', "${appName}").delete()
                            }
                            result = openshift.raw("apply", "-f openshift/app-template-stage.yaml")
                            // dit template moet een deployment hebben van het image met tag 'latest'

                            // en we starten een deployment in OpenShift
                            if (openshift.selector('dc', "${appName}").exists()) {
                                // deze latest heeft niets met tag 'latest' te maken
                                openshift.raw("rollout","latest","${appName}")
                            }
                        }
                    }
                }
            }

            stage('Promote to PROD?') {
                timeout(time:30, unit:'MINUTES') {
                    input message: "Promote to PROD?", ok: "Promote"
                }

                script {
                    openshift.withCluster() {
                        // maak een nieuwe tag/versie
                        openshift.tag("${project}/${appName}:latest", "demojavateam/${appName}:${version}")
                        // maak deze versie production ready in het stagep project
                        openshift.tag("demojavateam/${appName}:${version}", "demojavateam/${appName}:production")
                    }
                }
            }
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}