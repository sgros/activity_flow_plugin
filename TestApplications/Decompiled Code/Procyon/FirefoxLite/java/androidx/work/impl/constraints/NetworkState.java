// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints;

public class NetworkState
{
    private boolean mIsConnected;
    private boolean mIsMetered;
    private boolean mIsNotRoaming;
    private boolean mIsValidated;
    
    public NetworkState(final boolean mIsConnected, final boolean mIsValidated, final boolean mIsMetered, final boolean mIsNotRoaming) {
        this.mIsConnected = mIsConnected;
        this.mIsValidated = mIsValidated;
        this.mIsMetered = mIsMetered;
        this.mIsNotRoaming = mIsNotRoaming;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final NetworkState networkState = (NetworkState)o;
            if (this.mIsConnected != networkState.mIsConnected || this.mIsValidated != networkState.mIsValidated || this.mIsMetered != networkState.mIsMetered || this.mIsNotRoaming != networkState.mIsNotRoaming) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.mIsConnected) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2 = n;
        if (this.mIsValidated) {
            n2 = n + 16;
        }
        int n3 = n2;
        if (this.mIsMetered) {
            n3 = n2 + 256;
        }
        int n4 = n3;
        if (this.mIsNotRoaming) {
            n4 = n3 + 4096;
        }
        return n4;
    }
    
    public boolean isConnected() {
        return this.mIsConnected;
    }
    
    public boolean isMetered() {
        return this.mIsMetered;
    }
    
    public boolean isNotRoaming() {
        return this.mIsNotRoaming;
    }
    
    public boolean isValidated() {
        return this.mIsValidated;
    }
    
    @Override
    public String toString() {
        return String.format("[ Connected=%b Validated=%b Metered=%b NotRoaming=%b ]", this.mIsConnected, this.mIsValidated, this.mIsMetered, this.mIsNotRoaming);
    }
}
