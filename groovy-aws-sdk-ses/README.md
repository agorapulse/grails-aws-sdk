# groovy-aws-sdk-ses

Groovy library used by the plugin: [Grails AWS SDK S3S](https://github.com/agorapulse/grails-aws-sdk-ses)

## Usage

```` 
def awsSesMailer = new AwsSesMailer() 
awsSesMailer.initClient(accessKey, secretKey, regionName)
awsSesMailer.mail {
    to 'joe@gmail.com'
    subject 'Hi there'
    from 'peter@gmail.com'
}
```` 
