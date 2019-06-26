// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.ads;

import com.google.android.exoplayer2.util.Assertions;
import android.net.Uri;
import java.util.Arrays;

public final class AdPlaybackState
{
    public static final AdPlaybackState NONE;
    public final int adGroupCount;
    public final long[] adGroupTimesUs;
    public final AdGroup[] adGroups;
    public final long adResumePositionUs;
    public final long contentDurationUs;
    
    static {
        NONE = new AdPlaybackState(new long[0]);
    }
    
    public AdPlaybackState(final long... original) {
        final int length = original.length;
        this.adGroupCount = length;
        this.adGroupTimesUs = Arrays.copyOf(original, length);
        this.adGroups = new AdGroup[length];
        for (int i = 0; i < length; ++i) {
            this.adGroups[i] = new AdGroup();
        }
        this.adResumePositionUs = 0L;
        this.contentDurationUs = -9223372036854775807L;
    }
    
    private boolean isPositionBeforeAdGroup(final long n, final int n2) {
        if (n == Long.MIN_VALUE) {
            return false;
        }
        final long n3 = this.adGroupTimesUs[n2];
        boolean b = true;
        final boolean b2 = true;
        if (n3 == Long.MIN_VALUE) {
            final long contentDurationUs = this.contentDurationUs;
            boolean b3 = b2;
            if (contentDurationUs != -9223372036854775807L) {
                b3 = (n < contentDurationUs && b2);
            }
            return b3;
        }
        if (n >= n3) {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && AdPlaybackState.class == o.getClass()) {
            final AdPlaybackState adPlaybackState = (AdPlaybackState)o;
            if (this.adGroupCount != adPlaybackState.adGroupCount || this.adResumePositionUs != adPlaybackState.adResumePositionUs || this.contentDurationUs != adPlaybackState.contentDurationUs || !Arrays.equals(this.adGroupTimesUs, adPlaybackState.adGroupTimesUs) || !Arrays.equals(this.adGroups, adPlaybackState.adGroups)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public int getAdGroupIndexAfterPositionUs(final long n, final long n2) {
        if (n != Long.MIN_VALUE && (n2 == -9223372036854775807L || n < n2)) {
            int n3 = 0;
            while (true) {
                final long[] adGroupTimesUs = this.adGroupTimesUs;
                if (n3 >= adGroupTimesUs.length || adGroupTimesUs[n3] == Long.MIN_VALUE || (n < adGroupTimesUs[n3] && this.adGroups[n3].hasUnplayedAds())) {
                    break;
                }
                ++n3;
            }
            if (n3 >= this.adGroupTimesUs.length) {
                n3 = -1;
            }
            return n3;
        }
        return -1;
    }
    
    public int getAdGroupIndexForPositionUs(final long n) {
        int n2;
        for (n2 = this.adGroupTimesUs.length - 1; n2 >= 0 && this.isPositionBeforeAdGroup(n, n2); --n2) {}
        if (n2 < 0 || !this.adGroups[n2].hasUnplayedAds()) {
            n2 = -1;
        }
        return n2;
    }
    
    @Override
    public int hashCode() {
        return (((this.adGroupCount * 31 + (int)this.adResumePositionUs) * 31 + (int)this.contentDurationUs) * 31 + Arrays.hashCode(this.adGroupTimesUs)) * 31 + Arrays.hashCode(this.adGroups);
    }
    
    public static final class AdGroup
    {
        public final int count;
        public final long[] durationsUs;
        public final int[] states;
        public final Uri[] uris;
        
        public AdGroup() {
            this(-1, new int[0], new Uri[0], new long[0]);
        }
        
        private AdGroup(final int count, final int[] states, final Uri[] uris, final long[] durationsUs) {
            Assertions.checkArgument(states.length == uris.length);
            this.count = count;
            this.states = states;
            this.uris = uris;
            this.durationsUs = durationsUs;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && AdGroup.class == o.getClass()) {
                final AdGroup adGroup = (AdGroup)o;
                if (this.count != adGroup.count || !Arrays.equals(this.uris, adGroup.uris) || !Arrays.equals(this.states, adGroup.states) || !Arrays.equals(this.durationsUs, adGroup.durationsUs)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        public int getFirstAdIndexToPlay() {
            return this.getNextAdIndexToPlay(-1);
        }
        
        public int getNextAdIndexToPlay(int n) {
            ++n;
            while (true) {
                final int[] states = this.states;
                if (n >= states.length || states[n] == 0 || states[n] == 1) {
                    break;
                }
                ++n;
            }
            return n;
        }
        
        public boolean hasUnplayedAds() {
            return this.count == -1 || this.getFirstAdIndexToPlay() < this.count;
        }
        
        @Override
        public int hashCode() {
            return ((this.count * 31 + Arrays.hashCode(this.uris)) * 31 + Arrays.hashCode(this.states)) * 31 + Arrays.hashCode(this.durationsUs);
        }
    }
}
