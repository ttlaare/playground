// ------------------------------------------------------------------------------
// <auto-generated>
//
//     This code was generated.
//
//     - To turn off auto-generation set:
//
//         [TeamCity (AutoGenerate = false)]
//
//     - To trigger manual generation invoke:
//
//         nuke --generate-configuration TeamCity --host TeamCity
//
// </auto-generated>
// ------------------------------------------------------------------------------

import jetbrains.buildServer.configs.kotlin.v2018_1.*
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.*
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.*
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.*
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.*

version = "2018.2"

project {
    buildType(Test_P1T3)
    buildType(Test_P2T3)
    buildType(Test_P3T3)
    buildType(Test)
    buildType(Pack)
    buildType(Publish)

    buildTypesOrder = arrayListOf(Test_P1T3, Test_P2T3, Test_P3T3, Test, Pack, Publish)

    params {
        select (
            "env.Verbosity",
            label = "Verbosity",
            description = "Logging verbosity during build execution. Default is 'Normal'.",
            value = "Normal",
            options = listOf("Minimal" to "Minimal", "Normal" to "Normal", "Quiet" to "Quiet", "Verbose" to "Verbose"),
            display = ParameterDisplay.NORMAL)
        select (
            "env.Configuration",
            label = "Configuration",
            value = "Debug",
            options = listOf("Debug" to "Debug", "Release" to "Release"),
            display = ParameterDisplay.NORMAL)
        param(
            "teamcity.runner.commandline.stdstreams.encoding",
            "UTF-8"
        )
    }
}
object Test_P1T3 : BuildType({
    name = "🚦 Test 🧩 1/3"
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    steps {
        exec {
            path = "build.cmd"
            arguments = "Compile Test --skip --test-partition 1"
        }
    }
})
object Test_P2T3 : BuildType({
    name = "🚦 Test 🧩 2/3"
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    steps {
        exec {
            path = "build.cmd"
            arguments = "Compile Test --skip --test-partition 2"
        }
    }
})
object Test_P3T3 : BuildType({
    name = "🚦 Test 🧩 3/3"
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    steps {
        exec {
            path = "build.cmd"
            arguments = "Compile Test --skip --test-partition 3"
        }
    }
})
object Test : BuildType({
    name = "🚦 Test"
    type = Type.COMPOSITE
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
        showDependenciesChanges = true
    }
    artifactRules = "**/*"
    triggers {
        vcs {
            triggerRules = "+:**"
        }
        schedule {
            schedulingPolicy = daily {
                hour = 3
            }
            triggerRules = "+:**"
            triggerBuild = always()
            withPendingChangesOnly = false
            enableQueueOptimization = true
            param("cronExpression_min", "3")
        }
    }
    dependencies {
        snapshot(Test_P1T3) {
            onDependencyFailure = FailureAction.FAIL_TO_START
            onDependencyCancel = FailureAction.CANCEL
        }
        snapshot(Test_P2T3) {
            onDependencyFailure = FailureAction.FAIL_TO_START
            onDependencyCancel = FailureAction.CANCEL
        }
        snapshot(Test_P3T3) {
            onDependencyFailure = FailureAction.FAIL_TO_START
            onDependencyCancel = FailureAction.CANCEL
        }
        artifacts(Test_P1T3) {
            artifactRules = "**/*"
        }
        artifacts(Test_P2T3) {
            artifactRules = "**/*"
        }
        artifacts(Test_P3T3) {
            artifactRules = "**/*"
        }
    }
})
object Pack : BuildType({
    name = "📦 Pack"
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    steps {
        exec {
            path = "build.cmd"
            arguments = "Pack --skip"
        }
    }
})
object Publish : BuildType({
    name = "🚚 Publish"
    type = Type.DEPLOYMENT
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    steps {
        exec {
            path = "build.cmd"
            arguments = "Publish --skip"
        }
    }
    dependencies {
        snapshot(Pack) {
            onDependencyFailure = FailureAction.FAIL_TO_START
            onDependencyCancel = FailureAction.CANCEL
        }
        artifacts(Pack) {
        }
    }
})
