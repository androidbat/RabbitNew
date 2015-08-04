package com.c.rabbit.net.ihttp;

import com.c.rabbit.net.http.IHttpTask;


public interface HttpConnectionListener {

    public void downloadEnd(IHttpTask http, Object data);
}
