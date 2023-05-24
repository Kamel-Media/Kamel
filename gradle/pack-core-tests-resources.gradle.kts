/***
 * How to handle Web Workers "standard" syntax with webpack:
 * https://stackoverflow.com/a/41630622/1363742
 */
fun createWebpackConfig(){
    val rootProjectAbsPath = rootProject.projectDir.absolutePath
    val path = """"$rootProjectAbsPath/kamel-core/build/generated/moko/jsMain/iokamelcore/res""""
    val webpackConfig = File(projectDir, "webpack.config.d/pack-test-resources-generated.js")
    val configText =
        """const path = require('path');

const mokoResourcePath = path.resolve($path);

config.module.rules.push(
    {
        test: /\.(.*)/,
        include: [
            path.resolve(mokoResourcePath)
        ],
        type: 'asset/resource'
    }
);

config.resolve.modules.push(
    path.resolve(mokoResourcePath)
);"""
    webpackConfig.writeText(configText)
}

tasks.create("createPackResourcesWebpackConfig") {
    doFirst {
        createWebpackConfig()
    }
}

tasks.getByName("jsJar").dependsOn("createPackResourcesWebpackConfig")
