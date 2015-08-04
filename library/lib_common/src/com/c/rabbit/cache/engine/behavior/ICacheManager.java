/*
 * Copyright (c) 2012 Freeman TV. All rights reserved.
 */
package com.c.rabbit.cache.engine.behavior;

/***
 * @author mingrenhan
 */
public interface ICacheManager
{
    /**
     * Gets the cache attribute of the CacheHub object
     *
     * @param cacheName
     * @return
     */
    public abstract ICache getCache( String cacheName );
}
