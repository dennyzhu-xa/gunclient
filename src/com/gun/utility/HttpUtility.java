package com.gun.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

 
public class HttpUtility {
 
	private static final String DEFAULT_ENCODING = "UTF-8";
 
	/**
	 * GET方式请求
	 * 
	 * @param uri
	 *            服务器的uri要用物理IP或域名,不识别localhost或127.0.0.1形式!
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String get(String uri) throws ClientProtocolException,
			IOException {
		HttpGet httpGet = new HttpGet(uri);
		// new DefaultHttpClient()
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		throw new IOException("status is " + statusCode);
	}
 
	/**
	 * GET方式请求
	 * 
	 * @param uri 
	 *            服务器的uri要用物理IP或域名,不识别localhost或127.0.0.1形式!
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String get(String uri, Map<String, String> paramMap)
			throws ClientProtocolException, IOException {
 
		StringBuilder sb = new StringBuilder(uri);
		if (paramMap != null) {
			boolean isBegin = true;
			for (String key : paramMap.keySet()) {
				if (isBegin) {
					sb.append("?").append(key).append("=")
							.append(paramMap.get(key));
					isBegin = false;
				} else {
					sb.append("&").append(key).append("=")
							.append(paramMap.get(key));
				}
 
			}
		}
		HttpGet httpGet = new HttpGet(sb.toString());
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
//			String result = EntityUtils.toString(httpResponse.getEntity());
			return EntityUtils.toString(httpResponse.getEntity());
		}
		throw new IOException("status is " + statusCode);
	}
 
 
	/**
	 * POST方式请求
	 * 
	 * @param uri
	 *            服务器的uri要用物理IP或域名,不识别localhost或127.0.0.1形式!
	 * @param paramMap
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String post(String uri, Map<String, String> paramMap)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				params.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_ENCODING);
			return result;
		}
//		 JSONObject jsonObject = JSONObject.fromObject(result);
		throw new IOException("status is " + statusCode);
	}
	
	/**
	 * POST方式请求，UTF-8编码发送内容
	 * 
	 * @param uri
	 * @param paramMap
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postEncoding(String uri, Map<String, String> paramMap)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				params.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params,
					DEFAULT_ENCODING));
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			return EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_ENCODING);
		}
		throw new IOException("status is " + statusCode);
	}
 
	/**
	 * POST方式请求
	 * 
	 * @param uri
	 *            服务器的uri要用物理IP或域名,不识别localhost或127.0.0.1形式!
	 * @param paramMap
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String uri, Map<String, String> paramMap,
			Map<String, String> headers) throws ClientProtocolException,
			IOException {
		HttpPost httpPost = new HttpPost(uri);
		if (headers != null) {
			for (String key : headers.keySet()) {
				httpPost.setHeader(key, headers.get(key));
			}
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				params.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params,
					DEFAULT_ENCODING));
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			return EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_ENCODING);
		}
		throw new IOException("status is " + statusCode);
	}
 
 
	
	/**
	 * post请求发送传输obj对象
	 * 
	 * @param uri
	 * @param paramMap
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postOfObject(String uri, Object obj)
			throws ClientProtocolException, IOException {
		String params = JSON.toJSONString(obj);
		return HttpUtility.postJson(uri, params);
 
	}
 
	/**
	 * 针对http传输json数据处理
	 * 
	 * @param uri
	 * @param parameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postJson(String uri, String parameters)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		if (parameters != null) {
			StringEntity entity = new StringEntity(parameters,"UTF-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			return EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_ENCODING);
		}
		throw new IOException("status is " + statusCode);
	}
 
	/**
	 * 针对http传输json数据处理
	 * 
	 * @param uri
	 * @param parameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postByJson(String uri, String parameters)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		if (parameters != null) {
			StringEntity entity = new StringEntity(parameters,"UTF-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
		int statusCode;
		if ((statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
			return EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_ENCODING);
		}
		throw new IOException("status is " + statusCode);
	}
}


