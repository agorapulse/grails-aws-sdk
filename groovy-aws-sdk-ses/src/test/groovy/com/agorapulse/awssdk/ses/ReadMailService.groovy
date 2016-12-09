package com.agorapulse.awssdk.ses

import groovy.transform.CompileStatic

import javax.mail.*
import javax.mail.internet.MimeBodyPart

@CompileStatic
class ReadMailService {

    static String getMessageBodyAsAsAString(Message message) {
        try {
            Object content = message.getContent()
            if ( content instanceof String ) {
                return (String) content

            } else if ( content instanceof Multipart ) {
                return parseMultipart((Multipart) content)
            }
        }catch ( MessagingException e ) {
            e.printStackTrace()
        } catch ( IOException e ) {
            e.printStackTrace()
        }
    }

    static String parseMultipart(Multipart mPart ) {
        for ( int i = 0; i < mPart.getCount(); i++ ) {
            BodyPart bp = mPart.getBodyPart( i )
            String disposition = bp.getDisposition()
            if ( disposition == null && bp instanceof MimeBodyPart ){
                MimeBodyPart mbp = (MimeBodyPart) bp

                if ( mbp.isMimeType( 'text/plain' ) ) {
                    return  mbp.getContent()

                } else if ( mbp.isMimeType( 'text/html' ) ) {
                    return  (String) mbp.getContent()
                }
            }
        }
        null
    }

    static int deleteMessagesAtInboxWithSubject(String subject, String folder, String provider, String host, String email, String password) {

        def store = connectToStore(provider, host, email, password)
        def inbox = openInbox(store, folder)
        inbox.open(Folder.READ_WRITE)
        Message[] messages = inbox.getMessages()
        int numberOfDeletedMessages = 0
        for ( int i = 0; i < messages.size(); i++ ) {
            def message = messages[i]
            if ( message.subject == subject ) {
                message.setFlag(Flags.Flag.DELETED, true)
                numberOfDeletedMessages++
            }
        }
        inbox.close(true)
        store.close()
        numberOfDeletedMessages
    }

    static Properties instantiatProperties() {
        Properties props = new Properties()
        props.setProperty('mail.imap.ssl.enable', 'javax.net.ssl.SSLSocketFactory')
        props.setProperty('mail.imap.socketFactory.fallback', 'false')
        props.setProperty('mail.imap.ssl.enable', 'true')
        props
    }

    static Properties instantiateGmailProperties(String host) {
        Properties properties = new Properties()
        properties.put('mail.pop3.host', host)
        properties.put('mail.pop3.port', '995')
        properties.put('mail.pop3.starttls.enable', 'true')
        properties
    }

    static Store connectToStore(String provider, String host, String email, String password) {

        if ( email?.endsWith('@gmail.com') ) {
            Properties properties = instantiateGmailProperties(host)
            Session emailSession = Session.getDefaultInstance(properties)
            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore('pop3s')
            store.connect host, email, password
            return store

        }
        // Connect to the POP3 server
        def props = instantiatProperties()
        Session session = Session.getDefaultInstance props, null

        def store = session.getStore provider
        store.connect host, email, password
        store
    }

    static Folder openInbox(Store store, String folder) {
        // Open the folder
        Folder inbox = store.getFolder folder
        if (!inbox) {
            throw new Exception()
        }
        inbox
    }
}
