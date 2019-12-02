package com.weng.ueditor.upload.ceph.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.weng.framework.common.utils.ToolUtil;
import com.weng.ueditor.upload.ceph.config.CephConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * aws S3工具类
 */
@Slf4j
public class S3Util {

	private static S3Util instance = null;

	private S3Util() {
	}

	public static S3Util getInstance() {
		if (instance == null) {
			instance = new S3Util();
		}
		return instance;
	}

	public AmazonS3 getS3Client() {

		ClientConfiguration clientConfig = new ClientConfiguration();
		if (StringUtils.isNotEmpty(CephConfig.SIGNER_TYPE) ) { //设置用于签署此客户端请求的签名算法的名称。如果未设置或显式设置为null，客户端将根据服务和区域的支持的签名算法的配置文件选择使用的签名算法。
			clientConfig.setSignerOverride(CephConfig.SIGNER_TYPE);
		} else {
			clientConfig.setSignerOverride("S3SignerType");
		}
		if (Boolean.parseBoolean(CephConfig.IS_SECURE)) {
			clientConfig.setProtocol(Protocol.HTTPS);
		} else {
			clientConfig.setProtocol(Protocol.HTTP);
		}
		clientConfig.setClientExecutionTimeout(CephConfig.CLIENT_EXECUTION_TIMEOUT); //
		clientConfig.setRequestTimeout(CephConfig.REQUEST_TIMEOUT);
		clientConfig.withConnectionTimeout(CephConfig.CONNECTION_TIMEOUT);
		clientConfig.withSocketTimeout(CephConfig.SOCKET_TIMEOUT);
		clientConfig.withConnectionMaxIdleMillis(CephConfig.CONNECTION_MAXIDLE);
		// Allow as many retries as possible until the client executiaon timeout expires
		clientConfig.setMaxErrorRetry(CephConfig.MAX_ERROR_RETRY);

		AWSCredentials awsCredentials = new BasicAWSCredentials(CephConfig.ACCESS_KEY, CephConfig.SECRET_KEY);
		AmazonS3 s3client = new AmazonS3Client(awsCredentials, clientConfig);
		s3client.setEndpoint(CephConfig.ENDPOINT);
		s3client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
		return s3client;
	}

	public AmazonS3 getS3Client2() {
		ClientConfiguration clientConfig = new ClientConfiguration();
		if (ToolUtil.isNotEmpty(CephConfig.SIGNER_TYPE)) { //设置用于签署此客户端请求的签名算法的名称。如果未设置或显式设置为null，客户端将根据服务和区域的支持的签名算法的配置文件选择使用的签名算法。
			clientConfig.setSignerOverride(CephConfig.SIGNER_TYPE);
		} else {
			clientConfig.setSignerOverride("S3SignerType");
		}
		if (Boolean.parseBoolean(CephConfig.IS_SECURE)) {
			clientConfig.setProtocol(Protocol.HTTPS);
		} else {
			clientConfig.setProtocol(Protocol.HTTP);
		}
		clientConfig.setClientExecutionTimeout(CephConfig.CLIENT_EXECUTION_TIMEOUT); //
		clientConfig.setRequestTimeout(CephConfig.REQUEST_TIMEOUT);
		clientConfig.withConnectionTimeout(CephConfig.CONNECTION_TIMEOUT);
		clientConfig.withSocketTimeout(CephConfig.SOCKET_TIMEOUT);
		clientConfig.withConnectionMaxIdleMillis(CephConfig.CONNECTION_MAXIDLE);
		// Allow as many retries as possible until the client executiaon timeout expires
		clientConfig.setMaxErrorRetry(CephConfig.MAX_ERROR_RETRY);

		AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(CephConfig.ACCESS_KEY, CephConfig.SECRET_KEY));
		//TODO这里使用默认region,后续完善Region配置  Region cnNorth1 = Region.getRegion(Regions.CN_NORTH_1);
		AwsClientBuilder.EndpointConfiguration epConfig = new AwsClientBuilder.EndpointConfiguration (CephConfig.ENDPOINT, Regions.CN_NORTH_1.getName());

		AmazonS3 s3client = AmazonS3ClientBuilder
				.standard()
				.withEndpointConfiguration(epConfig)
				.withPathStyleAccessEnabled(true)
				.withClientConfiguration(clientConfig)
				.withCredentials(credentials)
				.build();

		return s3client;
	}


	/**
     * 获取bucket 前缀
     * @return
     */
	public String getPrefix() {
		String prefix;
		if (CephConfig.BUCKET_PREFIX != null) {
			prefix = CephConfig.BUCKET_PREFIX;
		} else {
			prefix = "test-";
		}
		return prefix;
	}

    /**
     * 生成bucket名称
     * @param prefix
     * @return
     */
	public String getBucketName(String prefix) {
		Random rand = new Random();
		int num = rand.nextInt(50);
		String randomStr = UUID.randomUUID().toString();

		return prefix + randomStr + num;
	}

    /**
     * 生成bucket名称
     * @return
     */
	public String getBucketName() {
		String prefix = getPrefix();
		Random rand = new Random();
		int num = rand.nextInt(50);
		String randomStr = UUID.randomUUID().toString();

		return prefix + randomStr + num;
	}






}
