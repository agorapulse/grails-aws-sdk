package grails.plugin.awssdk.s3

import agorapulse.libs.awssdk.util.AwsClientUtil
import com.amazonaws.AmazonClientException
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.*
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.Upload
import grails.core.GrailsApplication
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import groovy.util.logging.Slf4j
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.multipart.MultipartFile

@Slf4j
class AmazonS3Service implements InitializingBean {

    static SERVICE_NAME = AmazonS3Client.S3_SERVICE_NAME

    static final Map HTTP_CONTENTS = [
            audio: [contentType: 'audio/mpeg'],
            csv:   [contentType: 'text/csv', contentDisposition: 'attachment'],
            excel: [contentType: 'application/vnd.ms-excel', contentDisposition: 'attachment'],
            flash: [contentType: 'application/x-shockwave-flash'],
            pdf:   [contentType: 'application/pdf'],
            file:  [contentType: 'application/octet-stream', contentDisposition: 'attachment'],
            video: [contentType: 'video/x-flv']
    ]

    GrailsApplication grailsApplication
    AmazonS3 client
    TransferManager transferManager
    private String defaultBucketName = ''

    void afterPropertiesSet() throws Exception {
        // Set region
        Region region = AwsClientUtil.buildRegion(config, serviceConfig)
        assert region?.isServiceSupported(SERVICE_NAME)

        // Create client
        client = AmazonS3ClientBuilder.standard()
                .withRegion(region.name)
                .withCredentials(AwsClientUtil.buildCredentials(config, serviceConfig))
                .withClientConfiguration(AwsClientUtil.buildClientConfiguration(config, serviceConfig))
                .build()

        defaultBucketName = serviceConfig?.bucket ?: ''
    }

    protected void init(String bucketName) {
        defaultBucketName = bucketName
    }

    /**
     *
     * @param type
     * @param fileExtension
     * @param cannedAcl
     * @return
     */
    ObjectMetadata buildMetadataFromType(String type,
                                         String fileExtension,
                                         CannedAccessControlList cannedAcl = null) {
        Map contentInfo
        if (HTTP_CONTENTS[type]) {
            contentInfo = HTTP_CONTENTS[type] as Map
        } else if (type in ['image', 'photo']) {
            contentInfo = [contentType: "image/${fileExtension == 'jpg' ? 'jpeg' : fileExtension}"] // Return image/jpeg for images to fix Safari issue (download image instead of inline display)
        } else if (fileExtension == 'swf') {
            contentInfo = [contentType: "application/x-shockwave-flash"]
        } else {
            contentInfo = [contentType: 'application/octet-stream', contentDisposition: 'attachment']
        }

        ObjectMetadata metadata = new ObjectMetadata()
        metadata.setContentType(contentInfo.contentType)
        if (contentInfo.contentDisposition) {
            metadata.setContentDisposition(contentInfo.contentDisposition)
        }
        if (cannedAcl) {
            metadata.setHeader('x-amz-acl', cannedAcl.toString())
        }
        metadata
    }

    /**
     *
     * @param bucketName
     * @param region
     */
    void createBucket(String bucketName,
                      String region = '') {
        if (!region) {
            region = serviceConfig.region ?: config.region
        }
        if (!region || region == AwsClientUtil.DEFAULT_REGION) {
            client.createBucket(bucketName) // Default region US-EAST-1
        } else {
            client.createBucket(bucketName, region)
        }
    }

    /**
     *
     * @param bucketName
     */
    void deleteBucket(String bucketName) {
        client.deleteBucket(bucketName)
    }

    /**
     *
     * @param bucketName
     * @param key
     * @return
     */
    boolean deleteFile(String bucketName,
                       String key) {
        boolean deleted = false
        try {
            client.deleteObject(bucketName, key)
            deleted = true
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while deleting a file', exception
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while deleting a file', exception
        }
        deleted
    }

    /**
     *
     * @param key
     * @return
     */
    boolean deleteFile(String key) {
        assertDefaultBucketName()
        deleteFile(defaultBucketName, key)
    }

    /**
     *
     * @param String
     * @param bucketName
     * @param prefix
     * @return
     */
    boolean deleteFiles(String bucketName,
                        String prefix) {
        assert prefix.tokenize('/').size() >= 2, "Multiple delete are only allowed in sub/sub directories"
        boolean deleted = false
        try {
            ObjectListing objectListing = listObjects(bucketName, prefix)
            objectListing.objectSummaries?.each { S3ObjectSummary summary ->
                client.deleteObject(bucketName, summary.key)
            }
            deleted = true
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while deleting files', exception
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while deleting files', exception
        }
        deleted
    }

    /**
     *
     * @param prefix
     * @return
     */
    boolean deleteFiles(String prefix) {
        assertDefaultBucketName()
        deleteFiles(defaultBucketName, prefix)
    }

    /**
     *
     * @param String
     * @param bucketName
     * @param prefix
     * @return
     */
    boolean exists(String bucketName,
                   String prefix) {
        boolean exists = false
        if (!prefix) {
            return false
        }
        try {
            ObjectListing objectListing = client.listObjects(bucketName, prefix)
            if (objectListing.objectSummaries) {
                exists = true
            }
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while checking if file exists', exception
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while checking if file exists', exception
        }
        exists
    }

    /**
     *
     * @param prefix
     * @return
     */
    boolean exists(String prefix) {
        assertDefaultBucketName()
        exists(defaultBucketName, prefix)
    }

    /**
     *
     * @param bucketName
     * @param key
     * @param pathName
     * @return
     */
    File getFile(String bucketName,
                 String key,
                 String pathName) {
        File localFile = new File(pathName)
        client.getObject(new GetObjectRequest(bucketName, key), localFile)
        localFile
    }

    /**
     *
     * @param key
     * @param pathName
     * @return
     */
    File getFile(String key,
                 String pathName) {
        assertDefaultBucketName()
        getFile(defaultBucketName, key, pathName)
    }

    /**
     *
     * @return
     */
    List listBucketNames() {
        client.listBuckets().collect { it.name }
    }

    /**
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    ObjectListing listObjects(String bucketName,
                              String prefix) {
        client.listObjects(bucketName, prefix)
    }

    /**
     *
     * @param prefix
     * @return
     */
    ObjectListing listObjects(String prefix = '') {
        assertDefaultBucketName()
        listObjects(defaultBucketName, prefix)
    }

    /**
     *
     * @param String
     * @param bucketName
     * @param key
     * @param expirationDate
     * @return
     */
    String generatePresignedUrl(String bucketName,
                                String key,
                                Date expirationDate) {
        client.generatePresignedUrl(bucketName, key, expirationDate).toString()
    }

    /**
     *
     * @param key
     * @param expirationDate
     * @return
     */
    String generatePresignedUrl(String key,
                                Date expirationDate) {
        assertDefaultBucketName()
        generatePresignedUrl(defaultBucketName, key, expirationDate)
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param input
     * @param metadata
     * @return
     */
    String storeInputStream(String bucketName,
                            String path,
                            InputStream input,
                            ObjectMetadata metadata) {
        try {
            client.putObject(bucketName, path, input, metadata)
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while storing input stream', exception
            return ''
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while storing input stream', exception
            return ''
        }

        buildAbsoluteUrl(bucketName, path)
    }

    /**
     *
     * @param path
     * @param input
     * @param metadata
     * @return
     */
    String storeInputStream(String path,
                            InputStream input,
                            ObjectMetadata metadata) {
        assertDefaultBucketName()
        storeInputStream(defaultBucketName, path, input, metadata)
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param file
     * @param cannedAcl
     * @return
     */
    String storeFile(String bucketName,
                     String path,
                     File file,
                     CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file)
                    .withCannedAcl(cannedAcl)
            client.putObject(putObjectRequest)
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while storing file', exception
            return ''
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while storing file', exception
            return ''
        }

        buildAbsoluteUrl(bucketName, path)
    }

    /**
     *
     * @param path
     * @param file
     * @param cannedAcl
     * @return
     */
    String storeFile(String path,
                     File file,
                     CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        assertDefaultBucketName()
        storeFile(defaultBucketName, path, file, cannedAcl)
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param multipartFile
     * @param metadata
     * @return
     */
    String storeMultipartFile(String bucketName,
                              String path,
                              MultipartFile multipartFile,
                              CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead,
                              ObjectMetadata metadata = null) {
        if (!metadata) {
            metadata = new ObjectMetadata()
        }
        metadata.setHeader(Headers.S3_CANNED_ACL, cannedAcl.toString())
        metadata.setContentLength(multipartFile.size)
        byte[] resultByte = DigestUtils.md5(multipartFile.inputStream)
        metadata.setContentMD5(resultByte.encodeBase64().toString())
        metadata.setContentType(multipartFile.contentType)
        storeInputStream(bucketName, path, multipartFile.inputStream, metadata)
    }

    /**
     *
     * @param path
     * @param multipartFile
     * @param cannedAcl
     * @param metadata
     * @return
     */
    String storeMultipartFile(String path,
                              MultipartFile multipartFile,
                              CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead,
                              ObjectMetadata metadata = null) {
        assertDefaultBucketName()
        storeMultipartFile(defaultBucketName, path, multipartFile, cannedAcl, metadata)
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param file
     * @param cannedAcl
     * @param contentType
     * @return
     */
    Upload transferFile(String bucketName,
                        String path,
                        File file,
                        CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        if (!transferManager) {
            // Create transfer manager (only create if required, since it may pool connections and threads)
            transferManager = new TransferManager(client)
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file)
                .withCannedAcl(cannedAcl)
        transferManager.upload(putObjectRequest)
    }

    /**
     *
     * @param path
     * @param file
     * @param cannedAcl
     * @return
     */
    Upload transferFile(String path,
                        File file,
                        CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        assertDefaultBucketName()
        transferFile(defaultBucketName, path, file, cannedAcl)
    }

    /**
     * Move S3 object to different location (key).
     *
     * Moving objects is useful in combination with <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/how-to-set-lifecycle-configuration-intro.html">S3 Lifecycle Configurations</a> for prefixes.
     *
     * @param sourceBucketName      the name of the source bucket
     * @param sourceKey             the key of the source object
     * @param destinationBucketName the name of the destination bucket
     * @param destinationKey        the key of the destination object
     * @return the destination URL or <code>null</code> if the file wasn't moved
     */
    String moveObject(
            String sourceBucketName,
            String sourceKey,
            String destinationBucketName,
            String destinationKey
    ) {
        try {
            CopyObjectRequest request = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey)
            client.copyObject(request)
            client.deleteObject(sourceBucketName, sourceKey)
            return client.getUrl(destinationBucketName, destinationKey)
        } catch (AmazonClientException e) {
            log.error("Exception moving object $sourceBucketName/$sourceKey to $destinationBucketName/$destinationKey", e)
            return null
        }
    }

    static String getBucketFromUri(String aURI) {
        URI uri = new URI(aURI)
        String path = uri.path.startsWith('/') ? uri.path.substring(1, uri.path.length()) : uri.path
        if (uri.host) {
            // direct bucket URI not using any CNAME
            if (uri.host.endsWith('amazonaws.com')) {
                return path.substring(0, path.indexOf('/'))
            }
            return uri.host
        }
        // consider the bucket name is using CNAME of the same name as the bucket
        return path.substring(0, path.indexOf('/'))
    }

    static String getKeyFromUri(String aURI) {
        URI uri = new URI(aURI)
        String path = uri.path.startsWith('/') ? uri.path.substring(1, uri.path.length()) : uri.path
        if (uri.host) {
            // direct bucket URI not using any CNAME
            if (uri.host.endsWith('amazonaws.com')) {
                return path.substring(path.indexOf('/') + 1, path.length())
            }
            return path
        }
        // consider the bucket name is using CNAME of the same name as the bucket
        return path.substring(path.indexOf('/') + 1, path.length())
    }

    // PRIVATE

    boolean assertDefaultBucketName() {
        assert defaultBucketName, "Default bucket must be defined"
    }

    def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    def getServiceConfig() {
        config[SERVICE_NAME]
    }

    private String buildAbsoluteUrl(String bucketName,
                                    String path) {
        Region region = AwsClientUtil.buildRegion(config, serviceConfig)
        "https://${region.name == AwsClientUtil.DEFAULT_REGION ? 's3' : "s3-${region.name}"}.amazonaws.com/${bucketName}/${path}"

    }

}
