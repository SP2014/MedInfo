package com.starsoft.medinfo.asyncrssclient;

import org.apache.http.Header;

public interface AsyncRssResponseHandler {
    public void onSuccess(RssFeed rssFeed);
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error);
}
