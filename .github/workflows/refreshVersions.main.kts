#!/usr/bin/env kotlin
// Usage: $ .github/workflows/refreshVersions.main.kts

@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.23.0")
// Find latest version at https://github.com/krzema12/github-actions-kotlin-dsl/releases

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.peterjgrainger.ActionCreateBranchV2
import it.krzeminski.githubactions.actions.reposync.PullRequestV2
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.file.Paths

private val everyMondayAt7am = Cron(minute = "0", hour = "7", dayWeek = "1")

val branch = "dependency-update"
val commitMessage = "Refresh versions.properties"
val prTitle = "Upgrade gradle dependencies"
val prBody = "[refreshVersions](https://github.com/jmfayard/refreshVersions) has found those library updates!"
val javaSetup = SetupJavaV3(
    javaVersion = "17",
    distribution = SetupJavaV3.Distribution.Adopt,
)

val workflowRefreshVersions: Workflow = workflow(
    name = "RefreshVersions",
    on = listOf(
        Schedule(listOf(everyMondayAt7am)),
        WorkflowDispatch(),
    ),
    sourceFile = Paths.get(".github/workflows/refreshversions.main.kts"),
) {
    job(
        id = "Refresh-Versions",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(
            name = "check-out",
            action = CheckoutV3(ref = "main"),
        )
        uses(
            name = "setup-java",
            action = javaSetup,
        )
        uses(
            name = "create-branch",
            action = ActionCreateBranchV2(branch),
            env = linkedMapOf(
                "GITHUB_TOKEN" to expr { secrets.GITHUB_TOKEN },
            ),
        )
        uses(
            name = "gradle refreshVersions",
            action = GradleBuildActionV2(arguments = "refreshVersions"),
        )
        uses(
            name = "Commit",
            action = AddAndCommitV9(
                authorName = "GitHub Actions",
                authorEmail = "noreply@github.com",
                message = commitMessage,
                newBranch = branch,
                push = "--force --set-upstream origin dependency-update",
            ),
        )
        uses(
            name = "Pull Request",
            action = PullRequestV2(
                sourceBranch = branch,
                destinationBranch = "main",
                prTitle = prTitle,
                prBody = prBody,
                prDraft = true,
                githubToken = expr { secrets.GITHUB_TOKEN },
            ),
        )
    }

}
println("Updating ${workflowRefreshVersions.targetFileName}")
workflowRefreshVersions.writeToFile()