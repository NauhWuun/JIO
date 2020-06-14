package org.NauhWuun.jio.cloud.ali;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.io.*;

public class oss
{
	private static String endpoint = "<endpoint, http://oss-cn-hangzhou.aliyuncs.com>";
	private static String accessKeyId = "<accessKeyId>";
	private static String accessKeySecret = "<accessKeySecret>";
	private static String bucketName = "<bucketName>";
	private static String key = "<key>";

	public static void main(String[] args) throws IOException {
		/*
		 * Constructs a client instance with your account for accessing OSS
		 */
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		System.out.println("Getting Started with OSS SDK for Java\n");

		try {

			/*
			 * Determine whether the bucket exists
			 */
			if (!ossClient.doesBucketExist(bucketName)) {
				/*
				 * Create a new OSS bucket
				 */
				System.out.println("Creating bucket " + bucketName + "\n");
				ossClient.createBucket(bucketName);
				CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
				createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
				ossClient.createBucket(createBucketRequest);
			}

			/*
			 * List the buckets in your account
			 */
			System.out.println("Listing buckets");

			ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
			listBucketsRequest.setMaxKeys(500);

			for (Bucket bucket : ossClient.listBuckets()) {
				System.out.println(" - " + bucket.getName());
			}
			System.out.println();

			/*
			 * Upload an object to your bucket
			 */
			System.out.println("Uploading a new object to OSS from a file\n");
			ossClient.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));

			/*
			 * Determine whether an object residents in your bucket
			 */
			boolean exists = ossClient.doesObjectExist(bucketName, key);
			System.out.println("Does object " + bucketName + " exist? " + exists + "\n");

			ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
			ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.Default);

			ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, key);
			System.out.println("ACL:" + objectAcl.getPermission().toString());

			/*
			 * Download an object from your bucket
			 */
			System.out.println("Downloading an object");
			OSSObject object = ossClient.getObject(bucketName, key);
			System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
			displayTextInputStream(object.getObjectContent());

			/*
			 * List objects in your bucket by prefix
			 */
			System.out.println("Listing objects");
			ObjectListing objectListing = ossClient.listObjects(bucketName, "My");
			for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println(" - " + objectSummary.getKey() + "  " +
						"(size = " + objectSummary.getSize() + ")");
			}
			System.out.println();

			/*
			 * Delete an object
			 */
			System.out.println("Deleting an object\n");
			ossClient.deleteObject(bucketName, key);

		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorMessage());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} finally {
			/*
			 * Do not forget to shut down the client finally to release all allocated resources.
			 */
			ossClient.shutdown();
		}
	}

	private static File createSampleFile() throws IOException {
		File file = File.createTempFile("oss-java-sdk-", ".txt");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write("abcdefghijklmnopqrstuvwxyz\n");
		writer.write("0123456789011234567890\n");
		writer.close();

		return file;
	}

	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null) break;

			System.out.println("    " + line);
		}
		System.out.println();

		reader.close();
	}
}
