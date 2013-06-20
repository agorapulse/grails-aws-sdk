import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.ObjectListing
import grails.util.Metadata
import groovy.io.FileType

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsBootstrap")

target(loadConfig: "Load S3 resources config") {
    depends(parseArguments)

    if (argsMap['help']) {
        println USAGE
        exit 0
    }

    loadApp()
    configureApp()

    awsConfig = grailsApp.config.grails.plugin?.awssdk
    cdnConfig = grailsApp.config.grails.resources?.mappers?.cdn

    // Parse arguments
    bucket = argsMap['bucket'] ?: cdnConfig.s3?.bucket ?: awsConfig?.s3?.bucket ?: awsConfig?.bucket
    accessKey = argsMap['access-key'] ?: cdnConfig.s3?.accessKey ?: awsConfig?.s3?.accessKey ?: awsConfig?.accessKey
    secretKey = argsMap['secret-key'] ?: cdnConfig.s3?.secretKey ?: awsConfig?.s3?.secretKey ?: awsConfig?.secretKey
    recursive = argsMap['all'] ? true : false // Only used in status (forced to true in push)
    region = argsMap['region'] ?: cdnConfig.s3?.region ?: awsConfig?.s3?.region ?: awsConfig?.region ?: ''

    expirationDate = null
    def expires = argsMap['expires'] ?: cdnConfig.s3?.expires ?: 0
    if (expires) {
        if (expires instanceof Date) {
            expirationDate = expires
        } else if (expires instanceof Integer) {
            expirationDate = new Date() + expires
        }
    }

    prefix = argsMap['prefix'] ?: cdnConfig.prefix ?: ''
    if (!prefix.endsWith('/')) prefix = "$prefix/"
    if (prefix.startsWith('/')) prefix = prefix.replaceFirst('/', '')

    pluginsPrefix = argsMap['plugins-prefix'] ?: cdnConfig.pluginsPrefix ?: "${prefix}plugins/"
    if (!pluginsPrefix.endsWith('/')) pluginsPrefix = "$pluginsPrefix/"
    if (pluginsPrefix.startsWith('/')) pluginsPrefix = pluginsPrefix.replaceFirst('/', '')

    if (isPluginProject) {
        def pluginName = new Metadata().getApplicationName()
        def pluginVersion = appCtx.getBean('pluginManager').getGrailsPlugin(pluginName).version
        prefix = "$pluginsPrefix$pluginName-$pluginVersion/"
    }
}

target(loadModulesResources: "Load modules resources") {
    depends(loadConfig)

    modulesResources = [:]
    def grailsResourceProcessor = appCtx.getBean('grailsResourceProcessor')
    grailsResourceProcessor.modulesByName.each { name, module ->
        module.resources.each { meta ->
            modulesResources[meta.actualUrl] = meta.processedFile
        }
    }
}

target(loadWebAppResources: "Load web-app resources") {
    webAppResources = [:]
    def includeRegex = cdnConfig.includeRegex ?: /.*/
    def excludeRegex = cdnConfig.excludeRegex ?: /(?i)WEB-INF|META-INF|CNAME|LICENSE|node_modules|Gemfile|Makefile|Rakefile|.DS_Store|Spec\.js|\.coffee|\.ico|\.gitignore|\.html|\.json|\.less|\.md|\.npmignore|\.php|\.sh|\.scss|\.svn|\.yml|\/doc\/|\/docs\/|\/spec\/|\/src\/|\/test\/|\/tests\//
    new File("${basedir}/web-app").eachFileRecurse (FileType.FILES) { File file ->
        def relativePath = file.path.replace("${basedir}/web-app", '')
        if (relativePath.find(includeRegex) && !relativePath.find(excludeRegex)) {
            webAppResources[relativePath] = file
        }
    }
}

target(loadResources: "Load web-app and modules resources") {
    depends(loadWebAppResources, loadModulesResources)

    resources = webAppResources + modulesResources
}

target(loadResourceKeys: "Load appResourceKeys and pluginsResourceKeys") {
    depends(loadResources)

    appResourceKeys = []
    pluginsResourceKeys = []

    resources.each { String path, File file ->
        def key = path.replaceFirst('/', '')
        if (key.startsWith('plugins/')) {
            if (recursive) {
                pluginsResourceKeys << key.replaceFirst('plugins/' , '')
            } else {
                def pluginName = key.replaceFirst('plugins/' , '').tokenize('/').first()
                if (!(pluginName in pluginsResourceKeys)) {
                    pluginsResourceKeys << pluginName
                }
            }
        } else {
            if (recursive) {
                appResourceKeys << key
            } else {
                def directoryName = key.tokenize('/').first()
                if (!(directoryName in appResourceKeys)) {
                    appResourceKeys << directoryName
                }
            }
        }
    }
}

target(loadS3Client: "Load S3 Amazon Web Service") {
    depends(loadConfig)

    def amazonWebService = appCtx.getBean('amazonWebService')
    if (accessKey && secretKey) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey)
        s3 = new AmazonS3Client(credentials)
        if (region == 'us' || region == 'us-east-1') {
            s3.endpoint = "s3.amazonaws.com"
        } else {
            s3.endpoint = "s3-${region}.amazonaws.com"
        }
    } else {
        s3 = region ? amazonWebService.getS3(region) : amazonWebService.s3
    }
}

target(loadS3ResourceKeys: "Load S3 appResourceKeys and pluginsResourceKeys") {
    depends(loadConfig, loadS3Client)

    s3AppResourceKeys = []
    s3PluginsResourceKeys = []

    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
            .withBucketName(bucket)
            .withDelimiter(recursive ? '' : '/')

    ObjectListing appObjectListing = s3.listObjects(listObjectsRequest.withPrefix(prefix))
    ObjectListing pluginsObjectListing = s3.listObjects(listObjectsRequest.withPrefix(pluginsPrefix))

    if (recursive) {
        s3AppResourceKeys = collectAllObjectKeys(s3, appObjectListing)*.replace(prefix, '')
        s3PluginsResourceKeys = collectAllObjectKeys(s3, pluginsObjectListing)*.replace(pluginsPrefix, '')
    } else {
        // Check web-app directory/file names
        s3AppResourceKeys = appObjectListing.commonPrefixes.collect { it.tokenize('/').last() }
        s3AppResourceKeys.addAll(appObjectListing.objectSummaries.collect { it.key.tokenize('/').last() })
        // Check plugin names
        s3PluginsResourceKeys = pluginsObjectListing.commonPrefixes.collect { it.tokenize('/').last() }
    }

    s3ResourceKeys = (s3AppResourceKeys + s3PluginsResourceKeys).flatten()
}

// PRIVATE

List collectAllObjectKeys(AmazonS3Client s3, ObjectListing objectListing) {
    List keys = objectListing.objectSummaries*.key
    if (objectListing.truncated) {
        // Request returns more than 1000 items (default max keys)
        ObjectListing next = s3.listNextBatchOfObjects(objectListing)
        while (next.truncated) {
            current = s3.listNextBatchOfObjects(next)
            keys.addAll(current.objectSummaries.collect { it.key })
            next = s3.listNextBatchOfObjects(current)
        }
        keys.addAll(next.objectSummaries*.key)
    }
    return keys
}
