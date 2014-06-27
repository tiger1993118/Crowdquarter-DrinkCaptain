package com.crowdquarter.drinkcaptain.util;

import android.app.Application;

public class DrinkCaptainApplication extends Application {
	// http://developers.delivery.com/api-overview-food/

	// for development
	private String clientId = "Y2VhZWE5OTEwMjVmMzc0NTUwZTc1ZDQ3OTg0NzAzMjNm";
	private String clientSecret = "oeYWNxfWQFanZhKMauKMnMtOxdRPyJ5mFw0vTR7w";
	private String urlTarget = "http://sandbox.delivery.com";

	// //for production
	// private String clientId = "ZjdhYTIwZWQyZWEyNjU2YzlmNzJiN2QwYWNiYjVmNmQ5";
	// private String clientSecret = "NJlMvCdja9vXchHP0czKEc28pQrw81tYVz9JfY7t";
	// private String urlTarget = "https://api.delivery.com";

	private String urlRedirect = "http://www.mhytest.com";

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getUrlTarget() {
		return urlTarget;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}
}
