// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.net.Uri;
import java.util.HashSet;
import java.util.Set;

public final class ContentUriTriggers
{
    private final Set<Trigger> mTriggers;
    
    public ContentUriTriggers() {
        this.mTriggers = new HashSet<Trigger>();
    }
    
    public void add(final Uri uri, final boolean b) {
        this.mTriggers.add(new Trigger(uri, b));
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.mTriggers.equals(((ContentUriTriggers)o).mTriggers));
    }
    
    public Set<Trigger> getTriggers() {
        return this.mTriggers;
    }
    
    @Override
    public int hashCode() {
        return this.mTriggers.hashCode();
    }
    
    public int size() {
        return this.mTriggers.size();
    }
    
    public static final class Trigger
    {
        private final boolean mTriggerForDescendants;
        private final Uri mUri;
        
        Trigger(final Uri mUri, final boolean mTriggerForDescendants) {
            this.mUri = mUri;
            this.mTriggerForDescendants = mTriggerForDescendants;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && this.getClass() == o.getClass()) {
                final Trigger trigger = (Trigger)o;
                if (this.mTriggerForDescendants != trigger.mTriggerForDescendants || !this.mUri.equals((Object)trigger.mUri)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        public Uri getUri() {
            return this.mUri;
        }
        
        @Override
        public int hashCode() {
            return this.mUri.hashCode() * 31 + (this.mTriggerForDescendants ? 1 : 0);
        }
        
        public boolean shouldTriggerForDescendants() {
            return this.mTriggerForDescendants;
        }
    }
}
