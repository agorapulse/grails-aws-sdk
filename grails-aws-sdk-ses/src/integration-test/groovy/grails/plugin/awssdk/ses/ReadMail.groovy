package grails.plugin.awssdk.ses
import javax.mail.*
import javax.mail.internet.MimeBodyPart

class ReadMail {

    TestInboxConfig testInboxConfig

    static ReadMail withEnvironmentVariables() {
        String email = System.getProperty('TEST_INBOX_EMAIL')
        String host = System.getProperty('TEST_INBOX_HOST')
        String password = System.getProperty('TEST_INBOX_PASSWORD')
        String folder = System.getProperty('TEST_INBOX_FOLDER')
        String provider = System.getProperty('TEST_INBOX_PROVIDER')
        def testInboxConfig = new TestInboxConfig(email: email, host: host, password: password, folder: folder, provider: provider)
        new ReadMail(testInboxConfig: testInboxConfig)
    }

    static Properties instantiatProperties() {
        Properties props = new Properties()
        props.setProperty("mail.imap.ssl.enable", "javax.net.ssl.SSLSocketFactory")
        props.setProperty("mail.imap.socketFactory.fallback", "false")
        props.setProperty("mail.imap.ssl.enable", "true")
        props
    }

    Store connectToStore() {
        // Connect to the POP3 server
        def props = instantiatProperties()
        Session session = Session.getDefaultInstance props, null
        def store = session.getStore testInboxConfig.provider
        store.connect testInboxConfig.host, testInboxConfig.email, testInboxConfig.password
        store
    }

    Folder openInbox(Store store) {
        // Open the folder
        Folder inbox = store.getFolder testInboxConfig.folder
        if (!inbox) {
            throw new Exception()
        }
        inbox
    }

    static boolean hasAttachments(Message msg) throws MessagingException {
        if (msg.isMimeType("multipart/mixed")) {
            Multipart mp = (Multipart)msg.getContent();
            if (mp.getCount() > 1)
                return true;
        }
        return false;
    }

    List<String> fetchFolderMessageSubjects() throws Exception {
        def store = connectToStore()
        def inbox = openInbox(store)

        inbox.open(Folder.READ_ONLY)

        // Get the messages from the server
        Message[] messages = inbox.getMessages()
        List<String> subjects = []
        for(int i = 0; i < messages.size();i++) {
            subjects << messages[i].subject
        }
        // Close the connection
        // but don't remove the messages from the server

        inbox.close false
        store.close()

        subjects
    }

    int deleteMessagesAtInboxWithSubject(String subject) {
        def store = connectToStore()
        def inbox = openInbox(store)

        inbox.open(Folder.READ_WRITE)

        Message[] messages = inbox.getMessages()
        int numberOfDeletedMessages = 0
        for(int i = 0; i < messages.size();i++) {
            def message = messages[i]
            if(message.subject == subject) {
                message.setFlag(Flags.Flag.DELETED, true);
                numberOfDeletedMessages++
            }
        }
        inbox.close true
        store.close()

        numberOfDeletedMessages
    }

    String getMessageBodyAsAsAString(Message message) {
        try {
            Object content = message.getContent()
            if ( content instanceof String ) {
                return (String) content
            } else if ( content instanceof Multipart ) {
                return parseMultipart((Multipart) content)
            }
        }catch ( MessagingException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    boolean doMessagesWithSubjectAtInboxHaveAttachments(String subject) {
        def store = connectToStore()
        def inbox = openInbox(store)

        inbox.open(Folder.READ_WRITE)

        Message[] messages = inbox.getMessages()
        if(!messages) {
            return false
        }
        messages = messages.findAll { it.subject == subject }
        if(!messages) {
            return false
        }
        for(int i = 0; i < messages.size();i++) {
            def message = messages[i]
            if(!hasAttachments(message)) {
                return false
            }
        }
        true
    }

    List<Map> messagesWithSubjectAtInbox(String subject) {
        def result = []
        def store = connectToStore()
        def inbox = openInbox(store)

        inbox.open(Folder.READ_WRITE)

        Message[] messages = inbox.getMessages()
        int numberOfDeletedMessages = 0
        for(int i = 0; i < messages.size();i++) {
            def message = messages[i]
            if(message.subject == subject) {
                result << [subject: message.subject, body: getMessageBodyAsAsAString(message)]
            }
        }
        inbox.close false
        store.close()

        result
    }

    static String parseMultipart(Multipart mPart ) {
        for ( int i = 0; i < mPart.getCount(); i++ ) {
            BodyPart bp = mPart.getBodyPart( i );
            String disposition = bp.getDisposition()
            if ( disposition == null && bp instanceof MimeBodyPart ){
                MimeBodyPart mbp = (MimeBodyPart) bp

                if ( mbp.isMimeType( "text/plain" ) ) {
                    return  mbp.getContent();

                } else if ( mbp.isMimeType( "text/html" ) ) {
                    return  (String) mbp.getContent();
                }
            }
        }
        null
    }

}