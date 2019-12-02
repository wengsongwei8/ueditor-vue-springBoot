package com.weng.ueditor.common;

import java.io.File;

public class Constants {

    /*
     * 审批通过标识
     */
    public static final int APPROVE_PASS = 1;

    /*
     * 审批不通过标识
     */
    public static final int APPROVE_NOT_PASS = 0;

    /*
     * 资源状态标识:未审核
     */
    public static final int RESOURCE_STATUS_NOT_APPROVE = 0;

    /*
     * 资源状态标识:审核通过
     */
    public static final int RESOURCE_STATUS_APPROVE_PASS = 1;

    /*
     * 资源状态标识:审核未通过
     */
    public static final int RESOURCE_STATUS_APPROVE_NOT_PASS = 2;

    /**
     * 特殊符号定义
     */
    public static final String COMMA = ",";

    public static final String SLASH = "/";

    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String SEMICOLON = ";";

    public static final String QUESTION_MARK = "?";

    public static final String SQL_URL_SEPARATOR = "&";

    public static final String AT_SYMBOL = "@";

    public static final String OCTOTHORPE = "#";

    public static final String PERCENT_SIGN = "%";

    public static final String NEW_LINE_CHAR = "\n";

    public static final String COLON = ":";

    public static final String MINUS = "-";

    public static final String UNDERLINE = "_";

    public static final char CSV_HEADER_SEPARATOR = ':';

    public static final char DELIMITER_START_CHAR = '<';

    public static final char DELIMITER_END_CHAR = '>';

    public static final String PARENTHESES_START = "(";

    public static final String PARENTHESES_END = ")";

    public static final String SQUARE_BRACKET_START = "[";

    public static final String SQUARE_BRACKET_END = "]";


    public static final char ASSIGNMENT_CHAR = '=';

    public static final char DOLLAR_DELIMITER = '$';

    public static final String MYSQL_KEY_DELIMITER = "`";

    public static final String APOSTROPHE = "'";

    public static final String DOUBLE_QUOTES = "\"";

    /**
     * 统一认证开关配置
     */
    public static final String SECURITY_SSO_ENABLED = "security.sso.enabled";


    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "CURRENT_USER";


    /**
     * 当前平台
     */
    public static final String CURRENT_PLATFORM = "CURRENT_PLATFORM";


    /**
     * auth code key
     */
    public static final String AUTH_CODE = "authCode";


    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";

    /**
     * Token header名称
     */
    public static final String TOKEN_HEADER_STRING = "Authorization";

    /**
     * Token 用户名
     */
    public static final String TOKEN_USER_NAME = "token_user_name";

    /**
     * Token 密码
     */
    public static final String TOKEN_USER_PASWORD = "token_user_password";

    /**
     * Token 创建时间
     */
    public static final String TOKEN_CREATE_TIME = "token_create_time";


    public static final String SCHEDULE_JOB_DATA_KEY = "scheduleJobs";

    /**
     * 常用图片格式
     */
    public static final String REG_IMG_FORMAT = "^.+(.JPEG|.jpeg|.JPG|.jpg|.PNG|.png|.GIF|.gif)$";

    /**
     * 邮箱格式
     */
    public static final String REG_EMAIL_FORMAT = "^[a-z_0-9.-]{1,64}@([a-z0-9-]{1,200}.){1,5}[a-z]{1,6}$";

    /**
     * 敏感sql操作
     */
    public static final String REG_SENSITIVE_SQL = "drop\\s|alter\\s|grant\\s|insert\\s|replace\\s|delete\\s|truncate\\s|update\\s|remove\\s";


    /**
     * 匹配多行sql注解正则
     */
    public static final String REG_SQL_ANNOTATE = "(?ms)('(?:''|[^'])*')|--.*?$|/\\*[^+]*?\\*/";


    public static final String DIR_DOWNLOAD = File.separator + "download" + File.separator;

    public static final String DIR_EMAIL = File.separator + "email" + File.separator;

    public static final String DIR_TEMPL = File.separator + "tempFiles" + File.separator;

    public static final String HTTP_PROTOCOL = "http";

    public static final String HTTPS_PROTOCOL = "https";

    public static final String PROTOCOL_SEPARATOR = "://";


    public static final String QUERY_COUNT_SQL = "SELECT COUNT(*) FROM (%s) CT";

    public static final String QUERY_META_SQL = "SELECT * FROM (%s) MT WHERE 1=0";
}
