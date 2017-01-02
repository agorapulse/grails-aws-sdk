package agorapulse.libs.awssdk.s3

import com.amazonaws.services.s3.model.ObjectMetadata
import groovy.transform.CompileStatic

@CompileStatic
class ObjectMetadataUtils {

    /**
     *
     * @param type
     * @param fileExtension
     * @param cannedAcl
     * @return
     */
    @SuppressWarnings(['ElseBlockBraces', 'FactoryMethodName'])
    static ObjectMetadata buildMetadataFromType(String type,
                                         String fileExtension) {
        Map contentInfo
        if ( S3HttpContents.HTTP_CONTENTS[type] ) {
            contentInfo = S3HttpContents.HTTP_CONTENTS[type] as Map

        } else if (type in ['image', 'photo']) {
            contentInfo = [contentType: "image/${fileExtension == 'jpg' ? 'jpeg' : fileExtension}"]
            // Return image/jpeg for images to fix Safari issue (download image instead of inline display)

        } else if (fileExtension == 'swf') {
            contentInfo = [contentType: 'application/x-shockwave-flash']
        } else {
            contentInfo = [contentType: 'application/octet-stream', contentDisposition: 'attachment']
        }

        ObjectMetadata metadata = new ObjectMetadata()
        String contentType = contentInfo.contentType as String
        metadata.contentType = contentType
        if ( contentInfo.contentDisposition ) {
            metadata.contentDisposition = contentInfo.contentDisposition as String
        }
        metadata
    }
}
