package com.bumptech.glide.load.engine.cache;

import android.support.p001v4.util.Pools.Pool;
import com.adjust.sdk.Constants;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
    private final Pool<PoolableDigestContainer> digestPool = FactoryPools.threadSafe(10, new C04001());
    private final LruCache<Key, String> loadIdToSafeHash = new LruCache(Constants.ONE_SECOND);

    /* renamed from: com.bumptech.glide.load.engine.cache.SafeKeyGenerator$1 */
    class C04001 implements Factory<PoolableDigestContainer> {
        C04001() {
        }

        public PoolableDigestContainer create() {
            try {
                return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class PoolableDigestContainer implements Poolable {
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }

    public String getSafeKey(Key key) {
        Object obj;
        synchronized (this.loadIdToSafeHash) {
            obj = (String) this.loadIdToSafeHash.get(key);
        }
        if (obj == null) {
            obj = calculateHexStringDigest(key);
        }
        synchronized (this.loadIdToSafeHash) {
            this.loadIdToSafeHash.put(key, obj);
        }
        return obj;
    }

    private String calculateHexStringDigest(Key key) {
        PoolableDigestContainer poolableDigestContainer = (PoolableDigestContainer) this.digestPool.acquire();
        try {
            key.updateDiskCacheKey(poolableDigestContainer.messageDigest);
            String sha256BytesToHex = Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
            return sha256BytesToHex;
        } finally {
            this.digestPool.release(poolableDigestContainer);
        }
    }
}
