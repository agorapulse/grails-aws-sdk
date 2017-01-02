package agorapulse.libs.awssdk.s3

import groovy.transform.CompileStatic

@CompileStatic
class UploadResult {
    // The unique store id
    String storeId = '0'
    // The original file name
    String fileName = ''
    // The multipart form field name
    String fileField = ''
}
