package com.agorapulse.awssdk.ses

import groovy.transform.CompileStatic

@CompileStatic
class AwsSdkSesEmailDeliveryStatus {
    public static final int STATUS_DELIVERED = 1
    public static final int STATUS_BLACKLISTED = -1
    public static final int STATUS_NOT_DELIVERED = 0
}
