package com.naver.hackday.util.cache;

public class CachingKeyHelper {

    private static final String POSTNO_PREFIX = "post_no";
    private static final String PAGENO_PREFIX = "page_no";
    private static final String VALIDATION_PREFIX = "valid_key";
    private static final String DELIMITER = ":";
    private static final String REACT_USER_SET ="pst";
    public static final String REACT_LAST_TIME_KEY = "PstTime";

    public static String getCommentListKey(String postKey, String pageKey) {
        return String.format("%s%s%s%s%s%s%s",
                POSTNO_PREFIX,
                DELIMITER,
                postKey,
                DELIMITER,
                PAGENO_PREFIX,
                DELIMITER,
                pageKey);
    }

    public static String getValidationCommentListKey(String postKey, String pageKey) {
        return String.format("%s%s%s%s%s%s%s%s%s",
                VALIDATION_PREFIX,
                DELIMITER,
                POSTNO_PREFIX,
                DELIMITER,
                postKey,
                DELIMITER,
                PAGENO_PREFIX,
                DELIMITER,
                pageKey);
    }

    public static String getReactUserSetKey(String commentId) {
        return String.format("%s%s", REACT_USER_SET, commentId);
    }

}
