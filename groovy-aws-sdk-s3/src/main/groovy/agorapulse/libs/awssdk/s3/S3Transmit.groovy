package agorapulse.libs.awssdk.s3

import agorapulse.libs.awssdk.AwsSdkUtils
import com.amazonaws.AmazonClientException
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.Upload
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class S3Transmit {

    AmazonS3Client client
    TransferManager transferManager

    @SuppressWarnings('FactoryMethodName')
    static ObjectMetadata buildMetadataFromType(String type, String fileExtension) {
        ObjectMetadataUtils.buildMetadataFromType(type, fileExtension)
    }

    /**
     *
     * @param bucketName
     * @param regionName
     */
    @SuppressWarnings(['FactoryMethodName', 'BuilderMethodWithSideEffects'])
    void createBucket(String bucketName, String regionName) {
        client?.createBucket(bucketName, regionName)
    }

    /**
     *
     * @param bucketName
     */
    void deleteBucket(String bucketName) {
        client?.deleteBucket(bucketName)
    }

    /**
     *
     * @param bucketName
     * @param key
     * @return
     */
    boolean deleteFile(String bucketName, String key) {
        boolean deleted = false
        try {
            client?.deleteObject(bucketName, key)
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
     * @param String
     * @param bucketName
     * @param prefix
     * @return
     */
    boolean deleteFiles(String bucketName, String prefix) {
        assert prefix.tokenize('/').size() >= 2, 'Multiple delete are only allowed in sub/sub directories'
        boolean deleted = false
        try {
            ObjectListing objectListing = listObjects(bucketName, prefix)
            objectListing.objectSummaries?.each { S3ObjectSummary summary ->
                client?.deleteObject(bucketName, summary.key)
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
            ObjectListing objectListing = client?.listObjects(bucketName, prefix)
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
     * @param bucketName
     * @param key
     * @param pathName
     * @return
     */
    @SuppressWarnings('JavaIoPackageAccess')
    File getFile(String bucketName,
                 String key,
                 String pathName) {
        File localFile = new File(pathName)
        client?.getObject(new GetObjectRequest(bucketName, key), localFile)
        localFile
    }

    /**
     *
     * @return
     */
    List listBucketNames() {
        client?.listBuckets()*.name
    }

    /**
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    ObjectListing listObjects(String bucketName, String prefix) {
        client?.listObjects(bucketName, prefix)
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
        client?.generatePresignedUrl(bucketName, key, expirationDate)?.toString()
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param input
     * @param metadata
     * @return
     */
    String storeInputStream(String regionName,
                            String bucketName,
                            String path,
                            InputStream input,
                            ObjectMetadata metadata) {
        try {
            client?.putObject(bucketName, path, input, metadata)
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while storing input stream', exception
            return ''
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while storing input stream', exception
            return ''
        }

        buildAbsoluteUrl(regionName, bucketName, path)
    }

    /**
     *
     * @param bucketName
     * @param path
     * @param file
     * @param cannedAcl
     * @return
     */
    String storeFile(String regionName,
                     String bucketName,
                     String path,
                     File file,
                     CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file)
                    .withCannedAcl(cannedAcl)
            client?.putObject(putObjectRequest)
        } catch (AmazonS3Exception exception) {
            log.warn 'An amazon S3 exception was catched while storing file', exception
            return ''
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while storing file', exception
            return ''
        }

        buildAbsoluteUrl(regionName, bucketName, path)
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

        // Create transfer manager (only create if required, since it may pool connections and threads)
        transferManager = transferManager ?: new TransferManager(client)
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file)
                .withCannedAcl(cannedAcl)
        transferManager.upload(putObjectRequest)
    }

    @SuppressWarnings('FactoryMethodName')
    static String buildAbsoluteUrl(String regionName,
                                          String bucketName,
                                          String path) {
        "${rootAbsoluteUrl(regionName, bucketName)}/${path ?: ''}"
    }

    static String rootAbsoluteUrl(String regionName,
                                          String bucketName) {
        Region region = RegionUtils.getRegion(regionName)
        String preffix =  "${region.name == AwsSdkUtils.DEFAULT_REGION ? 's3' : "s3-${region?.name ?: ''}"}"
        "https://${preffix}.amazonaws.com/${bucketName}"
    }
}
