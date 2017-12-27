package com.pinpinbox.android.SelfMadeClass;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IndexSheet {

	public static final String secret_SN = "d9$kv3fk(ri3mv#d-kg05[vs)F;f2lg/";

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		String urlhttps = "https://platformvmage3.cloudapp.net/pinpinbox/index/api/login";
//		String urlhttp = "http://platformvmage3.cloudapp.net/pinpinbox/index/api/login";
//
//		//example
//		Map<String, String> data = new HashMap<String, String>();
//		String id = "lion@vmage.com.tw";
//		String pwd = "1111";
//		data.put("id", id);
//		data.put("pwd", pwd);
//		String sign = encodePPB(data);
//
//		String urlParameters = "id=" + id + "&pwd=" + pwd + "&sign=" + sign;
//		int httptype = 0;// http,1:https
//		if (httptype == 0) {
//			try {
//				URL serverUrl = new URL(urlhttp);
//				HttpURLConnection urlConnection;
//
//				urlConnection = (HttpURLConnection) serverUrl.openConnection();
//
//				// Indicate that we want to write to the HTTP request body
//				urlConnection.setDoOutput(true);
//				urlConnection.setRequestMethod("POST");
//
//				// Writing the post data to the HTTP request body
//				BufferedWriter httpRequestBodyWriter = new BufferedWriter(
//						new OutputStreamWriter(urlConnection.getOutputStream()));
//				httpRequestBodyWriter.write(urlParameters);
//				httpRequestBodyWriter.close();
//
//				// Reading from the HTTP response body
//				Scanner httpResponseScanner = new Scanner(
//						urlConnection.getInputStream());
//				while (httpResponseScanner.hasNextLine()) {
//					System.out.println(httpResponseScanner.nextLine());
//				}
//				httpResponseScanner.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				URL obj;
//				obj = new URL(urlhttps);
//				HttpsURLConnection con = (HttpsURLConnection) obj
//						.openConnection();
//				con.setRequestMethod("POST");
//				con.setRequestProperty("User-Agent", "Captin-Test");
//				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//				con.setDoOutput(true);
//				DataOutputStream wr = new DataOutputStream(
//						con.getOutputStream());
//				wr.writeBytes(urlParameters);
//				wr.flush();
//				wr.close();
//
//				int responseCode = con.getResponseCode();
//				System.out.println("\nSending 'POST' request to URL : "
//						+ urlhttps);
//				System.out.println("Post parameters : " + urlParameters);
//				System.out.println("Response Code : " + responseCode);
//
//				BufferedReader in = new BufferedReader(new InputStreamReader(
//						con.getInputStream()));
//				String inputLine;
//				StringBuffer response = new StringBuffer();
//
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//
//
//				System.out.println("Response: [" + response.toString() + "]");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
    }

	public static String encodePPB(Map<String, String> data) {
		String sign = "";

		List<String> keys = new ArrayList<String>(data.keySet());
		Collections.sort(keys);

		MyLog.Set("d", IndexSheet.class, keys.toString());

		String requestOriginal = "";

		for (int i = 0; i < keys.size(); i++) {
            requestOriginal += (keys.get(i) + "=" + data.get(keys.get(i)));
//			requestOriginal += (URLEncoder.encode(keys.get(i)) + "=" + URLEncoder.encode(data.get(keys.get(i))));
//			System.out.println("requestOriginal: " + requestOriginal);
			if (i != keys.size() - 1)
				requestOriginal += "&";
		}
		System.out.println("requestOriginal: " + requestOriginal);

		String requestLow = requestOriginal.toLowerCase();
//		System.out.println("requestLow: " + requestLow);
		
		requestLow += secret_SN;
//		System.out.println("requestLow: " + requestLow);
		
		String requestMD5 = md5(requestLow);
//		System.out.println("requestMD5: " + requestMD5);

		sign = requestMD5.toLowerCase();
//		System.out.println("sign: " + sign);

		return sign;
	}

	public static String encodePPB_by_object(Map<String, Object> data) {
		String sign = "";

		List<String> keys = new ArrayList<String>(data.keySet());
		Collections.sort(keys);

		String requestOriginal = "";

		for (int i = 0; i < keys.size(); i++) {
			requestOriginal += (keys.get(i) + "=" + data.get(keys.get(i)));
//			requestOriginal += (URLEncoder.encode(keys.get(i)) + "=" + URLEncoder.encode(data.get(keys.get(i))));
//			System.out.println("requestOriginal: " + requestOriginal);
			if (i != keys.size() - 1)
				requestOriginal += "&";
		}
//		System.out.println("requestOriginal: " + requestOriginal);

		String requestLow = requestOriginal.toLowerCase();
//		System.out.println("requestLow: " + requestLow);

		requestLow += secret_SN;
//		System.out.println("requestLow: " + requestLow);

		String requestMD5 = md5(requestLow);
//		System.out.println("requestMD5: " + requestMD5);

		sign = requestMD5.toLowerCase();
//		System.out.println("sign: " + sign);

		return sign;
	}

	public static String md5(String input) {
		String result = input;
		try {
			if (input != null) {
				MessageDigest md = MessageDigest.getInstance("MD5"); // or
																		// "SHA-1"
				md.update(input.getBytes("UTF-8"));
				BigInteger hash = new BigInteger(1, md.digest());
				result = hash.toString(16);
				while (result.length() < 32) { // 40 for SHA-1
					result = "0" + result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	public static String getMD5Hash(String string)
	{
		try 
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(string.getBytes("UTF-8"));
			byte[] digest = md5.digest();

			string = new String(digest, "UTF-8");
		} 
		catch (NoSuchAlgorithmException e1) 
		{
			e1.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		return string;
	}
	
	public static String md5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {
			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			digest.update(input.getBytes("UTF-8"));
			byte[] bbbb = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : bbbb) {
				sb.append(String.format("%02x", b & 0xff));
			}
			md5 = sb.toString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return md5;
	}*/
}
