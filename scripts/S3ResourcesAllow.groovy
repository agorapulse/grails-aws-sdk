import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration
import com.amazonaws.services.s3.model.CORSRule

import static com.amazonaws.services.s3.model.CORSRule.AllowedMethods.*

includeTargets << new File("${awsSdkPluginDir}/scripts/_S3Resources.groovy")

USAGE = """
    s3-resources-allow --origin=ORIGIN --rule-id=RULE_ID [--bucket=BUCKET] [--region=REGION] [--access-key=ACCESS_KEY] [--secret-key=SECRET_KEY]

where
    --origin        = Origin to allow.
                    Ex.: *.mydomain.com
                    (REQUIRED)

    --rule-id       = Rule id
                    (default: GetRule)

    ACCESS_KEY      = AWS access key.
                    (default: grails.resources.mappers.cdn.s3.accessKey
                        or grails.plugin.awssdk.s3.accessKey
                        or grails.plugin.awssdk.accessKey)

    SECRET_KEY      = AWS secret key.
                    (default: grails.resources.mappers.cdn.s3.secretKey
                        or grails.plugin.awssdk.s3.secretKey
                        or grails.plugin.awssdk.secretKey)

    BUCKET          = S3 bucket name.
                    (default: grails.resources.mappers.cdn.s3.bucket
                        or grails.plugin.awssdk.s3.bucket
                        or grails.plugin.awssdk.bucket)

    REGION          = S3 bucket region.
                    (default: grails.resources.mappers.cdn.s3.region
                        or grails.plugin.awssdk.s3.region
                        or grails.plugin.awssdk.region)
"""

target(main: "Add a CORS GET rule for a given origin and an AWS S3 bucket") {
    loadConfig() // Load config and parse arguments

    // Parse parameter
    String origin = argsMap['origin'] ?: ''
    String ruleId = argsMap['rule-id'] ?: 'GetRule'
    if (!origin) {
        println "Origin is a required argument, use 'grails help s3-resources-cors' to show usage."
        exit 1
    }

    println "Checking existing CORS rules for S3 bucket ($bucket)....."
    loadS3Client()

    CORSRule rule
    List rules = []
    BucketCrossOriginConfiguration bucketConfig = s3.getBucketCrossOriginConfiguration(bucket)
    if (bucketConfig) {
        bucketConfig.rules.each {
            println "-- Rule ID: $it.id"
            //println "MaxAgeSeconds: $it.maxAgeSeconds"
            println "AllowedMethod: $it.allowedMethods"
            println "AllowedOrigins: $it.allowedOrigins"
            //println "AllowedHeaders: $it.allowedHeaders"
            //println "ExposeHeader: $it.exposedHeaders"
        }
        println "--"
        rules = bucketConfig.rules
        rule = rules.find { it.id == ruleId } //{ origin in it.allowedOrigins && GET in it.allowedMethods }
    } else {
        bucketConfig = new BucketCrossOriginConfiguration()
    }
    if (!rule) {
        println "Adding CORS GET rule....."
        rules.add(new CORSRule()
                .withId(ruleId)
                .withAllowedMethods([GET])
                .withAllowedOrigins([origin]))
        bucketConfig.setRules(rules)
        s3.setBucketCrossOriginConfiguration(bucket, bucketConfig)
        println "CORS GET rule successfully added for origin '$origin' (rule ID: $rule.id)"
    } else {
        println "CORS GET rule already exists for origin '$origin' (rule ID: $rule.id)"
    }
}

setDefaultTarget(main)
