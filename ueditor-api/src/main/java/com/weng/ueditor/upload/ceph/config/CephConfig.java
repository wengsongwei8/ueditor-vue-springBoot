package com.weng.ueditor.upload.ceph.config;

import java.util.ResourceBundle;

/**
 *  ceph文件配置类
 *
 */
public class CephConfig {

	// ceph 访问秘钥Key
	public static final String ACCESS_KEY;
	// ceph 访问秘钥
	public static final String SECRET_KEY;
	// ceph 请求路径
	public static final String ENDPOINT;
	// ceph 是否使用https安全认证
	public static final String IS_SECURE;
	// ceph bucket前缀
	public static final String BUCKET_PREFIX;
	// 默认 bucket
	public static final String BUCKET_DEFAULT;
	// ceph url签名类型
	public static final String SIGNER_TYPE;

	// 客户端执行超时时间毫秒 600s
	public static final Integer CLIENT_EXECUTION_TIMEOUT;
	// 客户端请求超时时间毫秒 60s
	public static final Integer REQUEST_TIMEOUT;
	// 客户端连接超时时间毫秒 600s
	public static final Integer CONNECTION_TIMEOUT;
	// 客户端传输数据超时时间毫秒 600s 设置在连接超时并关闭之前，要在已建立的打开连接上传输的数据等待的时间
	public static final Integer SOCKET_TIMEOUT;
	// 客户端连接休眠时间毫秒
	public static final Integer CONNECTION_MAXIDLE;
	// 客户端重试最大次数
	public static final Integer MAX_ERROR_RETRY;

	static {

		// 读取配置文件
		ResourceBundle resource = ResourceBundle.getBundle("ceph");
		// 初始化常量值
		ACCESS_KEY = resource.getObject("ceph.access_key").toString().trim();
		SECRET_KEY = resource.getObject("ceph.secret_key").toString().trim();
		ENDPOINT = resource.getObject("ceph.endPoint").toString().trim();
		IS_SECURE = resource.getObject("ceph.is_secure").toString().trim();
		BUCKET_PREFIX = resource.getObject("ceph.bucket_prefix").toString().trim();
		BUCKET_DEFAULT = resource.getObject("ceph.bucket_default").toString().trim();
		SIGNER_TYPE = resource.getObject("ceph.signertype").toString().trim();
		CLIENT_EXECUTION_TIMEOUT = Integer.valueOf(resource.getObject("ceph.client_execution_timeout").toString().trim());
		REQUEST_TIMEOUT = Integer.valueOf(resource.getObject("ceph.request_timeout").toString().trim());
		CONNECTION_TIMEOUT = Integer.valueOf(resource.getObject("ceph.connection_timeout").toString().trim());
		SOCKET_TIMEOUT = Integer.valueOf(resource.getObject("ceph.socket_timeout").toString().trim().trim());
		CONNECTION_MAXIDLE = Integer.valueOf(resource.getObject("ceph.connection_maxidle").toString().trim());
		MAX_ERROR_RETRY = Integer.valueOf(resource.getObject("ceph.max_error_retry").toString().trim());

	}

}
