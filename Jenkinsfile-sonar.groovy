try {
    timeout(time: 20, unit: 'MINUTES') {
        def appName = "os-fase2-jeeapp"
        def project = ""
        def ocCmd = "oc " +
                " --server=https://openshift.default.svc.cluster.local" +
                " --certificate-authority=/run/secrets/kubernetes.io/serviceaccount/ca.crt"
        def mvnCmd = "mvn"
        def seleniumHubURL ="http://selenium-hub-ywb.cloudapps.belastingdienst.nl/wd/hub"

        node {
            stage("Initialize") {
                def token = sh(script: 'cat /var/run/secrets/kubernetes.io/serviceaccount/token', returnStdout: true)
                ocCmd = ocCmd + " --token=" + token
                project = env.PROJECT_NAME
                ocCmd = ocCmd + " -n ${project}"
            }
        }
        node("maven-jos-openjdk8") {
            stage("Git Checkout") {
                git branch: 'master', url: 'ssh://git@git.belastingdienst.nl:7999/~wolfj09/os-fase2-jeeapp.git'
            }
            stage("Build WAR") {
                sh "${mvnCmd} clean package -Popenshift"
                //stash name: "war", includes: "target/ROOT.war"
            }
            stage("Build Image") {
                //unstash name:"war"
                sh "oc start-build ${appName}-docker --from-file=target/ROOT.war -n ${project}"
                openshiftVerifyBuild bldCfg: "${appName}-docker", namespace: project, waitTime: '20', waitUnit: 'min'
            }
            stage("Deploy Image") {
                openshiftDeploy deploymentConfig: appName, namespace: project
            }

            stage ('Test and Analysis') {
                parallel(
                        'UI Testing': {
                            def appURL = sh(script: ocCmd + " get routes -l app=${appName} -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout: true)
                            sh(script: "mvn integration-test -Pintegration-test -Dseleniumhuburl=${seleniumHubURL} -Dseleniumtesturl=http://${appURL}")
                            archiveArtifacts artifacts: 'target/screeenshot.png'
                        },
                        'Static Analysis': {
                            sh "${mvnCmd} sonar:sonar -Dsonar.host.url=http://sonarqube:9000 -DskipTests=true"
                        }
                )
            }
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}