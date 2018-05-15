package com.naver.hackday.util.cache;

public class CachingKeyHelper {

    private static final String POST_PREFIX = "post_no";
    private static final String PAGE_PREFIX = "page_no";
    private static final String VALIDATION_PREFIX = "valid:comment:list";
    private static final String DELIMITER = ":";

    public static String getCommentListKey(String postKey, String pageKey) {
        return String.format("%s%s%s%s%s%s%s", POST_PREFIX, DELIMITER, postKey, DELIMITER, PAGE_PREFIX, DELIMITER, pageKey);
    }

    public static String getValidationCommentListKey() {
        return VALIDATION_PREFIX;
    }

}
