// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.util.pool.StateVerifier;
import com.bumptech.glide.util.Util;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import android.support.v4.util.Pools;

public class SafeKeyGenerator
{
    private final Pools.Pool<PoolableDigestContainer> digestPool;
    private final LruCache<Key, String> loadIdToSafeHash;
    
    public SafeKeyGenerator() {
        this.loadIdToSafeHash = new LruCache<Key, String>(1000);
        this.digestPool = FactoryPools.threadSafe(10, (FactoryPools.Factory<PoolableDigestContainer>)new FactoryPools.Factory<PoolableDigestContainer>() {
            public PoolableDigestContainer create() {
                try {
                    return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
                }
                catch (NoSuchAlgorithmException cause) {
                    throw new RuntimeException(cause);
                }
            }
        });
    }
    
    private String calculateHexStringDigest(final Key key) {
        final PoolableDigestContainer poolableDigestContainer = this.digestPool.acquire();
        try {
            key.updateDiskCacheKey(poolableDigestContainer.messageDigest);
            return Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
        }
        finally {
            this.digestPool.release(poolableDigestContainer);
        }
    }
    
    public String getSafeKey(final Key key) {
        Object o = this.loadIdToSafeHash;
        synchronized (o) {
            final String s = this.loadIdToSafeHash.get(key);
            // monitorexit(o)
            o = s;
            if (s == null) {
                o = this.calculateHexStringDigest(key);
            }
            synchronized (this.loadIdToSafeHash) {
                this.loadIdToSafeHash.put(key, (String)o);
                return (String)o;
            }
        }
    }
    
    private static final class PoolableDigestContainer implements Poolable
    {
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier;
        
        PoolableDigestContainer(final MessageDigest messageDigest) {
            this.stateVerifier = StateVerifier.newInstance();
            this.messageDigest = messageDigest;
        }
        
        @Override
        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }
}
