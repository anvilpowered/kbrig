import jetbrains.buildServer.configs.kotlin.BuildFeatures
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.project
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubIssues
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.version

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

version = "2023.11"

project {

    buildType(Test())
    buildType(Style())
    buildType(Publish())

    features {
        githubIssues {
            id = "PROJECT_EXT_3"
            displayName = "anvilpowered/kbrig"
            repositoryURL = "https://github.com/anvilpowered/kbrig"
            authType = accessToken {
                accessToken = "credentialsJSON:cd9fa95f-cb19-4a1c-87ad-e02c6ad4ade3"
            }
            param("tokenId", "")
        }
    }
}

fun BuildType.configureVcs() {
    vcs {
        root(DslContext.settingsRoot)
    }
}

fun BuildType.configureTriggers() {
    triggers {
        vcs {
            branchFilter = """
                +:master
                +:dev
                +:dev-*
                +:renovate/*
            """.trimIndent()
        }
    }
}

fun BuildFeatures.configureBaseFeatures() {
    perfmon {}
    commitStatusPublisher {
        vcsRootExtId = "${DslContext.settingsRoot.id}"
        publisher = github {
            githubUrl = "https://api.github.com"
            authType = personalToken {
                token = "credentialsJSON:446a66f5-8a12-41b4-a31f-6eb9ee6087ba"
            }
        }
    }
}

fun BuildFeatures.configurePullRequests() {
    pullRequests {
        vcsRootExtId = "${DslContext.settingsRoot.id}"
        provider = github {
            authType = token {
                token = "credentialsJSON:a30ebfc3-045a-4821-9f62-f061490d2987"
            }
            filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER_OR_COLLABORATOR
        }
    }
}

class Test : BuildType() {
    init {
        name = "test"

        configureVcs()
        configureTriggers()
        features {
            configureBaseFeatures()
            configurePullRequests()
        }

        steps {
            gradle {
                id = "gradle_runner"
                tasks = "test"
            }
        }
    }
}

class Style : BuildType() {
    init {
        name = "style"

        configureVcs()
        configureTriggers()
        features {
            configureBaseFeatures()
            configurePullRequests()
        }

        steps {
            gradle {
                id = "gradle_runner"
                tasks = "ktlintCheck"
            }
        }
    }
}

class Publish : BuildType() {
    init {
        name = "publish"

        configureVcs()
        features {
            configureBaseFeatures()
        }

        steps {
            gradle {
                id = "gradle_runner"
                tasks = "publish"
            }
        }
    }
}
