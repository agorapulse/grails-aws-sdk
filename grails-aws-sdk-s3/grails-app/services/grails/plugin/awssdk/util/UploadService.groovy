package grails.plugin.awssdk.util

import grails.core.GrailsApplication
import groovy.util.logging.Slf4j
import org.springframework.web.multipart.MultipartFile

import java.util.zip.GZIPInputStream

@Slf4j
class UploadService {

    static lazyInit = false
    static transactional = false

    static int CONNECT_TIMEOUT = 15000
    static int READ_TIMEOUT = 60000

    GrailsApplication grailsApplication

    File downloadFile(String urlString) {
        if (!urlString) {
            return null
        }

        try {
            URL url = new URL(urlString)
            String fileName = urlString.replaceAll('https://', '').replaceAll('http://', '').replaceAll('/', '-')
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("$uploadPath/$fileName"))

            // Detect potential redirects (Code 3xx)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection()
            connection.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
            connection.setRequestProperty('Accept', '*/*')
            connection.setRequestProperty('Accept-Encoding', 'gzip, deflate')
            connection.connectTimeout = CONNECT_TIMEOUT
            connection.readTimeout = READ_TIMEOUT
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                url = new URL(connection.getHeaderField('Location'))
            }
            connection.disconnect()

            URLConnection redirectConnection = url.openConnection()
            redirectConnection.connectTimeout = CONNECT_TIMEOUT
            redirectConnection.readTimeout = READ_TIMEOUT

            // uncompressing if needed
            if (redirectConnection?.contentEncoding?.contains('gzip')) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(redirectConnection.inputStream)
                out << gzipInputStream
            } else {
                out << redirectConnection.inputStream
            }

            out.close()
            File file = new File("$uploadPath/$fileName")
            return file
        } catch (SocketTimeoutException exception) {
            log.warn 'A socket timeout exception was catched while downloading file', exception
            return null
        } catch (FileNotFoundException exception) {
            log.warn 'A file not found exception was catched while downloading file', exception
            return null
        } catch (IOException exception) {
            log.warn 'An IO exception was catched while downloading file', exception
            return null
        }
    }

	void uploadFile(MultipartFile multipartFile, fileName) {
		multipartFile.transferTo(new File("${uploadPath}/${fileName}"))
	}

    // PRIVATE

    def getConfig() {
        grailsApplication.config.grails?.plugins?.awssdk ?: grailsApplication.config.grails?.plugin?.awssdk
    }

    private String getUploadPath() {
        config['s3'] ? config['s3'].uploadPath ?: '/var/tmp' : '/var/tmp'
    }


}
