package grails.plugin.awssdk.ses

import groovy.transform.CompileStatic

@CompileStatic
class TestInboxConfig {
    String email
    String host
    String password
    String folder
    String provider


    static final String configurationMessage = """
            This test connects to a server to ensure that the email has been really delivered.
            You have to set the next environment variables:
               TEST_INBOX_EMAIL=your.email@gmx.es
               TEST_INBOX_HOST=pop.gmx.com
               TEST_INBOX_PASSWORD=yourpassword
               TEST_INBOX_FOLDER=INBOX
               TEST_INBOX_PROVIDER=pop3
If you are running this test in IntellJ you can configure environment variables for all your tests
with 'Edit Configuration -> Deafults -> Junit -> Environment Variables
"""

}
