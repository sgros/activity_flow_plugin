// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.database.CharArrayBuffer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class CursorWindow extends android.database.CursorWindow implements Parcelable
{
    public static final Parcelable$Creator<CursorWindow> CREATOR;
    private int mStartPos;
    private long nWindow;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<CursorWindow>() {
            public CursorWindow createFromParcel(final Parcel parcel) {
                return new CursorWindow(parcel, 0);
            }
            
            public CursorWindow[] newArray(final int n) {
                return new CursorWindow[n];
            }
        };
    }
    
    public CursorWindow(final Parcel parcel, final int n) {
        super(true);
        final IBinder strongBinder = parcel.readStrongBinder();
        this.mStartPos = parcel.readInt();
        this.native_init(strongBinder);
    }
    
    public CursorWindow(final boolean b) {
        super(b);
        this.mStartPos = 0;
        this.native_init(b);
    }
    
    private native boolean allocRow_native();
    
    private native void close_native();
    
    private native char[] copyStringToBuffer_native(final int p0, final int p1, final int p2, final CharArrayBuffer p3);
    
    private native void freeLastRow_native();
    
    private native byte[] getBlob_native(final int p0, final int p1);
    
    private native double getDouble_native(final int p0, final int p1);
    
    private native long getLong_native(final int p0, final int p1);
    
    private native int getNumRows_native();
    
    private native String getString_native(final int p0, final int p1);
    
    private native int getType_native(final int p0, final int p1);
    
    private native boolean isBlob_native(final int p0, final int p1);
    
    private native boolean isFloat_native(final int p0, final int p1);
    
    private native boolean isInteger_native(final int p0, final int p1);
    
    private native boolean isNull_native(final int p0, final int p1);
    
    private native boolean isString_native(final int p0, final int p1);
    
    private native void native_clear();
    
    private native IBinder native_getBinder();
    
    private native void native_init(final IBinder p0);
    
    private native void native_init(final boolean p0);
    
    public static CursorWindow newFromParcel(final Parcel parcel) {
        return (CursorWindow)CursorWindow.CREATOR.createFromParcel(parcel);
    }
    
    private native boolean putBlob_native(final byte[] p0, final int p1, final int p2);
    
    private native boolean putDouble_native(final double p0, final int p1, final int p2);
    
    private native boolean putLong_native(final long p0, final int p1, final int p2);
    
    private native boolean putNull_native(final int p0, final int p1);
    
    private native boolean putString_native(final String p0, final int p1, final int p2);
    
    private native boolean setNumColumns_native(final int p0);
    
    public boolean allocRow() {
        this.acquireReference();
        try {
            return this.allocRow_native();
        }
        finally {
            this.releaseReference();
        }
    }
    
    public void clear() {
        this.acquireReference();
        try {
            this.mStartPos = 0;
            this.native_clear();
        }
        finally {
            this.releaseReference();
        }
    }
    
    public void close() {
        this.releaseReference();
    }
    
    public void copyStringToBuffer(final int n, final int n2, final CharArrayBuffer charArrayBuffer) {
        if (charArrayBuffer == null) {
            throw new IllegalArgumentException("CharArrayBuffer should not be null");
        }
        if (charArrayBuffer.data == null) {
            charArrayBuffer.data = new char[64];
        }
        this.acquireReference();
        try {
            final char[] copyStringToBuffer_native = this.copyStringToBuffer_native(n - this.mStartPos, n2, charArrayBuffer.data.length, charArrayBuffer);
            if (copyStringToBuffer_native != null) {
                charArrayBuffer.data = copyStringToBuffer_native;
            }
        }
        finally {
            this.releaseReference();
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    protected void finalize() {
        if (this.nWindow == 0L) {
            return;
        }
        this.close_native();
    }
    
    public void freeLastRow() {
        this.acquireReference();
        try {
            this.freeLastRow_native();
        }
        finally {
            this.releaseReference();
        }
    }
    
    public byte[] getBlob(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.getBlob_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public double getDouble(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.getDouble_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public float getFloat(final int n, final int n2) {
        this.acquireReference();
        try {
            return (float)this.getDouble_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public int getInt(int n, final int n2) {
        this.acquireReference();
        try {
            n = (int)this.getLong_native(n - this.mStartPos, n2);
            return n;
        }
        finally {
            this.releaseReference();
        }
    }
    
    public long getLong(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.getLong_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public int getNumRows() {
        this.acquireReference();
        try {
            return this.getNumRows_native();
        }
        finally {
            this.releaseReference();
        }
    }
    
    public short getShort(final int n, final int n2) {
        this.acquireReference();
        try {
            return (short)this.getLong_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public int getStartPosition() {
        return this.mStartPos;
    }
    
    public String getString(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.getString_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public int getType(int type_native, final int n) {
        this.acquireReference();
        try {
            type_native = this.getType_native(type_native - this.mStartPos, n);
            return type_native;
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean isBlob(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.isBlob_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean isFloat(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.isFloat_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean isLong(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.isInteger_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean isNull(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.isNull_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean isString(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.isString_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    protected void onAllReferencesReleased() {
        this.close_native();
        super.onAllReferencesReleased();
    }
    
    public boolean putBlob(final byte[] array, final int n, final int n2) {
        this.acquireReference();
        try {
            return this.putBlob_native(array, n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean putDouble(final double n, final int n2, final int n3) {
        this.acquireReference();
        try {
            return this.putDouble_native(n, n2 - this.mStartPos, n3);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean putLong(final long n, final int n2, final int n3) {
        this.acquireReference();
        try {
            return this.putLong_native(n, n2 - this.mStartPos, n3);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean putNull(final int n, final int n2) {
        this.acquireReference();
        try {
            return this.putNull_native(n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean putString(final String s, final int n, final int n2) {
        this.acquireReference();
        try {
            return this.putString_native(s, n - this.mStartPos, n2);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public boolean setNumColumns(final int numColumns_native) {
        this.acquireReference();
        try {
            return this.setNumColumns_native(numColumns_native);
        }
        finally {
            this.releaseReference();
        }
    }
    
    public void setStartPosition(final int mStartPos) {
        this.mStartPos = mStartPos;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeStrongBinder(this.native_getBinder());
        parcel.writeInt(this.mStartPos);
    }
}
