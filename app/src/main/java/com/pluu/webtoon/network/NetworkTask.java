package com.pluu.webtoon.network;

import android.net.Uri;

import java.util.Map;

import com.pluu.support.impl.IRequest;
import com.pluu.support.impl.NetworkSupportApi;
import com.pluu.webtoon.common.LoggingInterceptor;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Network Request Task
 * <br/> 실제 Request 하는 로직
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NetworkTask {

	private final OkHttpClient client = new OkHttpClient();

	public String requestApi(final IRequest request) throws Exception {
		Request.Builder builder = new Request.Builder();

		for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}

		if (NetworkSupportApi.POST.equals(request.getMethod())) {
			// POST
			FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();
			for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
				encodingBuilder.add(entry.getKey(), entry.getValue());
			}
			RequestBody requestBody = encodingBuilder.build();
			builder.post(requestBody);
			builder.url(request.getId());
		} else {
			// GET
			Uri.Builder uriBuilder = new Uri.Builder();
			uriBuilder.encodedPath(request.getId());
			for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
				uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
			}
			builder.url(uriBuilder.build().toString());
		}

		return requestApi(builder.build());
	}

	public String requestApi(Request request) throws Exception {
		client.interceptors().add(new LoggingInterceptor());
		Response response = client.newCall(request).execute();
		String result = response.body().string();
//		Log.i(Const.LOG_TAG, "Response: " + result);
		return result;
	}


}