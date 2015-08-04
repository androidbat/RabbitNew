/*
 * Copyright (c) 2012 Freeman TV. All rights reserved.
 */
package com.c.rabbit.cache.diskcache;

import java.io.Serializable;

import com.c.rabbit.cache.engine.CacheElement;
import com.c.rabbit.cache.engine.behavior.IElementAttributes;


public class NoValueCacheElement extends CacheElement {

    private static final long serialVersionUID = -5308843980211277105L;

    public NoValueCacheElement(String cacheName, Serializable key, Serializable val, IElementAttributes attrArg) {
        super(cacheName, key, null, attrArg);
    }

    public Serializable getVal() {
        return null;
    }

}
