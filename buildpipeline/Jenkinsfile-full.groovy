try {
    timeout(time: 20, unit: 'MINUTES') {
        def appName = "os-fase2-jeeapp"
        def project = ""
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
                git branch: 'master', url: 'ssh://git@git.belastingdienst.nl:7999/~wolfj09/os-fase2-jeeapp.git'
            }
            stage("Build WAR") {
                sh "${mvnCmd} clean package -Popenshift"
                stash name: "all"
            }
            stage("Build Image") {
                sh "oc start-build ${appName}-docker --from-file=target/ROOT.war -n ${project}"
                openshiftVerifyBuild bldCfg: "${appName}-docker", namespace: project, waitTime: '20', waitUnit: 'min'
            }
            stage("Deploy Image") {
                openshiftDeploy deploymentConfig: appName, namespace: project
            }
        }
        stage('Test and Analysis') {
            parallel(
                    'UI Testing': {
                        node("jos-m3-openjdk8") {
                            unstash name: "all"
                            // service discovery..app
                            def appURL = sh(script: ocCmd + " get routes -l app=${appName} -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout: true)
                            // service discovery..selenium Hub
                            def seleniumHubURL = sh(script: ocCmd + " get routes -l app=selenium-grid -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout: true)
                            seleniumHubURL = "http://" + seleniumHubURL + "/wd/hub"

                            sh(script: "mvn integration-test -Pintegration-test -Dseleniumhuburl=${seleniumHubURL} -Dseleniumtesturl=http://${appURL}")
                            archiveArtifacts artifacts: 'target/screeenshot.png'
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
            stage('Sanity check') {
                input "Does the staging environment look ok?"
            }

            stage('Deploy - Production') {
                sh 'echo implement'
            }
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}