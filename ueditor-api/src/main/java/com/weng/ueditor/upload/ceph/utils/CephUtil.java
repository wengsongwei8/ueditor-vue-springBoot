package com.weng.ueditor.upload.ceph.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * ceph操作工具类
 * @author wengzhonghui
 * @date 2019/11/5 16:22
 */
public class CephUtil {

    /**
     * 获取所有bucket
     *
     * @return
     */
    public static List<Bucket> listBucket() {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.listBuckets();
    }

    /**
     * 获取正则分段上传的MultipartUpload列表
     *
     * @param bucketName
     * @return
     */
    public static List<MultipartUpload> listMultipartByBucket(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        MultipartUploadListing list = s3Client.listMultipartUploads(new ListMultipartUploadsRequest(bucketName));
        return list.getMultipartUploads();
    }

    /**
     * 创建bucketName
     *
     * @param bucketName
     * @return
     */
    public static Bucket createBucket(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.createBucket(bucketName);
    }

    /**
     * 删除bucket，同时删除bucket里面的对象
     *
     * @param bucketName
     * @return
     */
    public static boolean deleteBucket(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        try {
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            while (true) {
                for (Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
                    S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                    s3Client.deleteObject(bucketName, summary.getKey());
                }
                // more objectListing to retrieve?
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }

            VersionListing versionListing = s3Client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
            while (true) {
                for (Iterator<?> iterator =
                     versionListing.getVersionSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3VersionSummary vs = (S3VersionSummary) iterator.next();
                    s3Client.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                }
                if (versionListing.isTruncated()) {
                    versionListing = s3Client.listNextBatchOfVersions(versionListing);
                } else {
                    break;
                }
            }

            s3Client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 获取bucket存储位置
     *
     * @param bucketName
     * @return
     */
    public static String getBucketLocation(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.getBucketLocation(bucketName);
    }

    /**
     * 获取bucke策略
     *
     * @param bucketName
     * @return
     */
    public static BucketPolicy getBucketPolicy(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.getBucketPolicy(bucketName);
    }

    /**
     * 获取对象列表
     *
     * @param bucketName
     * @return
     */
    public static ObjectListing listObjects(String bucketName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.listObjects(bucketName);
    }

    /**
     * 文件上传
     *
     * @param bucketName
     * @param keyName     文件名称，eg:hellow.txt
     * @param inputStream
     * @param metadata
     * @return
     */
    public static PutObjectResult putObject(String bucketName, String keyName, InputStream inputStream, ObjectMetadata metadata) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            return s3Client.putObject(new PutObjectRequest(bucketName, keyName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 分片上传
     *
     * @param bucketName
     * @param keyName
     * @param file
     * @param metadata
     * @return
     */
    public static String multipartUpload(String bucketName, String keyName, File file, ObjectMetadata metadata) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, keyName);
        InitiateMultipartUploadResult initResult = s3Client.initiateMultipartUpload(request);
        List<PartETag> partETags = new ArrayList<PartETag>();
        try {
            long contentLength = file.length();
            long partSize = 5242880; // Set part size to 5 MB.

            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).withKey(keyName)
                        .withUploadId(initResult.getUploadId()).withPartNumber(i).withFileOffset(filePosition)
                        .withFile(file).withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
                    initResult.getUploadId(), partETags);

            s3Client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResult.getUploadId()));
        }
        return initResult.getUploadId();
    }


    /**
     * 获取对象http访问链接
     * @param bucketName
     * @param keyName
     * @param expireDate 链接失效日期
     * @return
     */
    public static URL generatePresignedUrl(String bucketName, String keyName, Date expireDate) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.generatePresignedUrl(bucketName, keyName,expireDate, HttpMethod.GET);
    }

    /**
     * 获取对象
     * @param bucketName
     * @param keyName
     * @return
     */
    public static S3Object getObject(String bucketName, String keyName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.getObject(bucketName, keyName);
    }

    /**
     * 下载对象
     * @param bucketName
     * @param keyName
     * @return
     */
    public static void downloadObject(String bucketName, String keyName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        try {
            S3Object o = s3Client.getObject(bucketName, keyName);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(keyName));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 获取对象属性
     * @param bucketName
     * @param keyName
     * @return
     */
    public static ObjectMetadata getObjectMetadata(String bucketName, String keyName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return  s3Client.getObjectMetadata(bucketName, keyName);
    }

    /**
     * 获取正则分片上传的对象信息
     * @param bucketName
     * @param keyName
     * @param uploadId
     * @return
     */
    public static List<PartSummary> listMultipartObject(String bucketName, String keyName,String uploadId) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        PartListing list = s3Client.listParts(new ListPartsRequest(bucketName, keyName, uploadId));
        return list.getParts();
    }

    /**
     * 中断对象分片上传
     * @param bucketName
     * @param keyName
     */
    public static void abortMultipart(String bucketName, String keyName,String uploadId) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        s3Client.deleteObject(bucketName, keyName);
        s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
    }


    /**
     * 对象复制，从源bucket复制到目标bucket
     * @param fromBucketName
     * @param fromKeyName
     * @param toBucketName
     * @param toKeyName
     * @return
     */
    public static CopyObjectResult copyObject(String fromBucketName, String fromKeyName, String toBucketName, String toKeyName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        return s3Client.copyObject(fromBucketName, fromKeyName, toBucketName, toKeyName);
    }

    /**
     * 删除Object
     * @param bucketName
     * @param keyName
     */
    public static void deleteObject(String bucketName, String keyName) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        s3Client.deleteObject(bucketName, keyName);
    }
    /**
     * 删除Objects
     * @param bucketName
     * @param objectkeys
     */
    public static void deleteObjects(String bucketName, String[] objectkeys) {
        AmazonS3 s3Client = S3Util.getInstance().getS3Client();
        DeleteObjectsRequest dor = new DeleteObjectsRequest(bucketName).withKeys(objectkeys);
        s3Client.deleteObjects(dor);
    }

    public static String genarateObjKey(String fileName ){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return UUID.randomUUID()+"."+suffix;
    }
}
