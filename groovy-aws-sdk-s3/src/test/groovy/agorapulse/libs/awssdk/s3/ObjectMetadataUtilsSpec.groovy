package agorapulse.libs.awssdk.s3

import com.amazonaws.services.s3.model.ObjectMetadata
import spock.lang.Specification
import spock.lang.Unroll

class ObjectMetadataUtilsSpec extends Specification {

    @Unroll
    def "test objectMetadataUtils inference for type: #type and fileExtension: #fileExtension"(String type, String fileExtension, ObjectMetadata expected) {

        when:
        def result = ObjectMetadataUtils.buildMetadataFromType(type, fileExtension)

        then:
        result.contentType == expected.contentType
        result.contentDisposition == expected.contentDisposition

        where:
        type    | fileExtension || expected
        'audio' | null          || new ObjectMetadata(contentType: 'audio/mpeg')
        'csv'   | null          || new ObjectMetadata(contentType: 'text/csv', contentDisposition: 'attachment')
        'excel' | null          || new ObjectMetadata(contentType: 'application/vnd.ms-excel', contentDisposition: 'attachment')
        'flash' | null          || new ObjectMetadata(contentType: 'application/x-shockwave-flash')
        'pdf'   | null          || new ObjectMetadata(contentType: 'application/pdf')
        'file'  | null          || new ObjectMetadata(contentType: 'application/octet-stream', contentDisposition: 'attachment')
        'video' | null          || new ObjectMetadata(contentType: 'video/x-flv')
        'image' | 'jpg'         || new ObjectMetadata(contentType: 'image/jpeg')
        'photo' | 'jpg'         || new ObjectMetadata(contentType: 'image/jpeg')
        null    | 'swf'         || new ObjectMetadata(contentType: 'application/x-shockwave-flash')
        'virus' | 'exe'         || new ObjectMetadata(contentType: 'application/octet-stream', contentDisposition: 'attachment')
  }
}
