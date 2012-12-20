includeTargets << new File("${awsSdkPluginDir}/scripts/_S3Resources.groovy")

USAGE = """
    s3-resources-status [--all] [--bucket=BUCKET] [--region=REGION] [--access-key=ACCESS_KEY] [--secret-key=SECRET_KEY]  [--prefix=PREFIX] [--plugins-prefix=PLUGIN_PREFIX]

where
    --all     = Perform a full recursive check of all resources.
                    (default: false, it only checks first level keys)

    BUCKET          = S3 bucket name.
                    (default: grails.resources.mappers.cdn.s3.bucket
                        or grails.plugin.awssdk.s3.bucket
                        or grails.plugin.awssdk.bucket)

    REGION          = S3 bucket region.
                    (default: grails.resources.mappers.cdn.s3.region
                        or grails.plugin.awssdk.s3.region
                        or grails.plugin.awssdk.region)

    ACCESS_KEY      = AWS access key.
                    (default: grails.resources.mappers.cdn.s3.accessKey
                        or grails.plugin.awssdk.s3.accessKey
                        or grails.plugin.awssdk.accessKey)

    SECRET_KEY      = AWS secret key.
                    (default: grails.resources.mappers.cdn.s3.secretKey
                        or grails.plugin.awssdk.s3.secretKey
                        or grails.plugin.awssdk.secretKey)

    PREFIX          = S3 key prefix
                    (default: grails.resources.mappers.cdn.prefix
                        or '')

    PLUGINS_PREFIX  = S3 key plugins prefix
                    (default: grails.resources.mappers.cdn.pluginPrefix
                        or grails.resources.mappers.cdn.prefix
                        or '')
"""

target(main: "Check static resources status in a AWS S3 bucket") {
    loadConfig() // Load config and parse arguments

    if (!accessKey) {
        println "Access key is required, use 'grails help s3-resources-status' to show usage."
        exit 1
    }
    if (!secretKey) {
        println "Secret key is required, use 'grails help s3-resources-status' to show usage."
        exit 1
    }
    if (!bucket) {
        println "Bucket is required, use 'grails help s3-resources-status' to show usage."
        exit 1
    }

    println "# Checking resources status in S3 bucket ($bucket)....."

    loadResourceKeys() // Load appResourceKeys and pluginsResourceKeys
    loadS3ResourceKeys() // Load s3AppResourceKeys and s3PluginsResourceKeys

    List deployedAppResourceKeys = appResourceKeys.findAll { it in s3AppResourceKeys }.sort()
    List notDeployedAppResourceKeys = appResourceKeys.findAll { !(it in s3AppResourceKeys) }.sort()
    List deployedPluginResourceKeys = pluginsResourceKeys.findAll { it in s3PluginsResourceKeys }.sort()
    List notDeployedPluginResourceKeys = pluginsResourceKeys.findAll { !(it in s3PluginsResourceKeys) }.sort()

    println '#'
    deployedAppResourceKeys.each { key ->
        println "#      \u001B[32mdeployed: ${prefix}${key}\u001B[0m"
    }
    if (deployedAppResourceKeys) println '#'
    notDeployedAppResourceKeys.each { key ->
        println "#      \u001B[31mnot deployed: ${prefix}${key}\u001B[0m"
    }
    if (notDeployedAppResourceKeys) println '#'
    deployedPluginResourceKeys.each { key ->
        println "#      \u001B[32mdeployed: ${pluginsPrefix}${key}\u001B[0m"
    }
    if (deployedPluginResourceKeys) println '#'
    notDeployedPluginResourceKeys.each { key ->
        println "#      \u001B[31mnot deployed: ${pluginsPrefix}${key}\u001B[0m"
    }
    if (notDeployedPluginResourceKeys) println '#'

    println "S3 resources status complete."
}

setDefaultTarget(main)

