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
        node("jos-robotframework") {
            catchError {
                stage("Git Checkout") {
                    git branch: 'master', url: 'ssh://git@git.belastingdienst.nl:7999/~wolfj09/os-fase2-jeeapp.git'
                }
                stage("Robot Testing") {
                    // service discovery..app
                    def appURL = sh(script: ocCmd + " get routes -l app=${appName} -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout:true)
                    appURL = "htttp://" + appURL
                    // service discovery..selenium Hub
                    def seleniumHubURL = sh(script: ocCmd + " get routes -l app=selenium-grid -o template --template {{range.items}}{{.spec.host}}{{end}}", returnStdout:true)
                    seleniumHubURL = "http://" + seleniumHubURL + "/wd/hub"
                    dir ('src/test/robot') {
                        sh('chmod +x ./runtests.sh')
                        sh(script: "./runtests.sh ${seleniumHubURL} ${appURL}")
                    }
                }
            }
            archive 'src/test/robot/output/*'
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}