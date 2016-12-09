package grails.plugin.awssdk.util

class ResultsPage<T> {

    static final int DEFAULT_PAGE_SIZE = 20

    List<T> items = []

    String offset

}
