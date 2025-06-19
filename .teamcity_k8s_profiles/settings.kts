import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.kotlinFile
import jetbrains.buildServer.configs.kotlin.kubernetesCloudImage
import jetbrains.buildServer.configs.kotlin.kubernetesCloudProfile
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesExecutor
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.03"

project {

    buildType(Build)

    features {
        amazonEC2CloudImage {
            id = "PROJECT_EXT_227"
            profileId = "amazon-46"
            agentPoolId = "-2"
            name = "Ubuntu AWS"
            vpcSubnetId = "subnet-043178c302cabfe37,subnet-0c4f70b91d8800740"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@gmail.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        kubernetesCloudImage {
            id = "PROJECT_EXT_228"
            profileId = "kube-8"
            agentPoolId = "-2"
            agentNamePrefix = "k8s-agent-latest"
            maxInstancesCount = 1
            podSpecification = runContainer {
                dockerImage = "jetbrains/teamcity-agent:latest"
            }
        }
        kubernetesExecutor {
            id = "PROJECT_EXT_229"
            connectionId = "PROJECT_EXT_2"
            profileName = "K8s"
            buildsLimit = "1"
            description = "K8s Executor (1 pod)"
            templateName = "arm64-buntu-pwsh-agent"
        }
        amazonEC2CloudProfile {
            id = "amazon-46"
            name = "AWS EC2"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AmazonWebServicesAws_2"
            maxInstancesCount = 1
        }
        kubernetesCloudProfile {
            id = "kube-8"
            name = "K8s Agents"
            terminateIdleMinutes = 30
            apiServerURL = "https://A51B42A65F7E54005C95A4D353916627.gr7.eu-west-1.eks.amazonaws.com"
            maxInstancesCount = 1
            authStrategy = eks {
                accessId = "AKIA5JH2VERVI62P5XDY"
                secretKey = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
                clusterName = "tc-dkrupkina-eks-cluster"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        kotlinFile {
            id = "kotlinScript"
            path = "jvm/main-kts/scripts/kotlin-hello-world.kts"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
