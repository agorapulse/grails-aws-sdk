import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest

import java.text.DateFormat
import java.text.SimpleDateFormat

includeTargets << new File("${awsSdkPluginDir}/scripts/_S3Resources.groovy")

USAGE = """
    s3-resources-push [--update] [--bucket=BUCKET] [--region=REGION] [--access-key=ACCESS_KEY] [--secret-key=SECRET_KEY] [--prefix=PREFIX] [--plugins-prefix=PLUGIN_PREFIX]

where
    --update        = Upload all resources even if they already exists.
                    (default: false, it only uploads new resources, not yet uploaded)

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
                    (default: grails.resources.mappers.cdn.prefix)

    PLUGINS_PREFIX  = S3 key plugins prefix
                    (default: grails.resources.mappers.cdn.pluginPrefix
                        or grails.resources.mappers.cdn.prefix)
"""

target(main: "Upload static resources to an AWS S3 bucket") {
    loadConfig() // Load config and parse arguments

    if (!accessKey) {
        println "Access key is required, use 'grails help s3-resources-push' to show usage."
        exit 1
    }
    if (!secretKey) {
        println "Secret key is required, use 'grails help s3-resources-push' to show usage."
        exit 1
    }
    if (!bucket) {
        println "Bucket is required, use 'grails help s3-resources-push' to show usage."
        exit 1
    }

    recursive = true
    update = argsMap['update'] ? true : false

    if (expirationDate) {
        println "Expiration date set to $expirationDate"
    }

    loadConfig() // Load config, prefix and pluginsPrefix
    loadS3Client() // Load s3

    loadResourceKeys() // Load appResourceKeys and pluginsResourceKeys

    int uploadCount = 0
    Map files = [:]
    List s3Keys = []
    if (update) {
        // Upload all resources
        resources.each { String key, File file ->
            key = key.replaceFirst('/', '')
            if (key.startsWith('plugins')) {
                files["$pluginsPrefix${key.replaceFirst('plugins/' , '')}"] = file
                s3Keys << "$pluginsPrefix${key.replaceFirst('plugins/' , '')}"
            } else {
                files["$prefix$key"] = file
                s3Keys << "$prefix$key"
            }
        }
    } else {
        // Only upload resources not yet deployed
        println "Checking resources status in S3 bucket ($bucket)....."
        loadS3ResourceKeys() // Load s3AppResourceKeys and s3PluginsResourceKeys

        List appResourceKeysToPush = appResourceKeys.findAll { !(it in s3AppResourceKeys) }.sort()
        List pluginsResourceKeysToPush = pluginsResourceKeys.findAll { !(it in s3PluginsResourceKeys) }.sort()

        appResourceKeysToPush.each { String key ->
            files["$prefix$key"] = resources["/$key"]
            s3Keys << "$prefix$key"
        }
        pluginsResourceKeysToPush.each { String key ->
            files["$pluginsPrefix$key"] = resources["/plugins/$key"]
            s3Keys << "$pluginsPrefix$key"
        }
    }

    // Push resources to bucket
    s3Keys.each { String s3Key ->
        println "Uploading $s3Key ..."
        File file = files[s3Key]
        s3.putObject(
                new PutObjectRequest(bucket, s3Key, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                        .withMetadata(buildMetaData(file.name.tokenize('.').last(), expirationDate))
        )
        uploadCount++
    }

    if (uploadCount) {
        println "S3 resources push complete: $uploadCount resources uploaded to bucket ($bucket)"
    } else {
        println "S3 resources push complete: nothing to push, all resources are already uploaded to bucket ($bucket)"
    }

}

setDefaultTarget(main)

// PRIVATE

private ObjectMetadata buildMetaData(String extension, Date expirationDate) {
    def metaData = new ObjectMetadata()
    if (expirationDate) {
        DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        metaData.setHeader("Cache-Control", "PUBLIC, max-age=${(expirationDate.time / 1000).toInteger()}, must-revalidate")
        metaData.setHeader("Expires", httpDateFormat.format(expirationDate))
    }
    // Specify content type for web fonts
    switch(extension) {
        case 'eot':
            metaData.setContentType('application/vnd.ms-fontobject')
            break
        case 'otf':
            metaData.setContentType('font/opentype')
            break
        case 'svg':
            metaData.setContentType('image/svg+xml')
            break
        case 'ttf':
            metaData.setContentType('application/x-font-ttf')
            break
        case 'woff':
            metaData.setContentType('application/x-font-woff')
            break
    }
    return metaData
}

private putObjects() {

}