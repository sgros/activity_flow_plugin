package android.support.p001v4.content;

import android.support.p001v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* renamed from: android.support.v4.content.Loader */
public class Loader<D> {
    boolean mAbandoned;
    boolean mContentChanged;
    int mId;
    OnLoadCompleteListener<D> mListener;
    boolean mProcessingChange;
    boolean mReset;
    boolean mStarted;

    /* renamed from: android.support.v4.content.Loader$OnLoadCompleteListener */
    public interface OnLoadCompleteListener<D> {
    }

    /* Access modifiers changed, original: protected */
    public void onAbandon() {
    }

    /* Access modifiers changed, original: protected */
    public boolean onCancelLoad() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onReset() {
    }

    /* Access modifiers changed, original: protected */
    public void onStartLoading() {
    }

    /* Access modifiers changed, original: protected */
    public void onStopLoading() {
    }

    public void unregisterListener(OnLoadCompleteListener<D> onLoadCompleteListener) {
        if (this.mListener == null) {
            throw new IllegalStateException("No listener register");
        } else if (this.mListener == onLoadCompleteListener) {
            this.mListener = null;
        } else {
            throw new IllegalArgumentException("Attempting to unregister the wrong listener");
        }
    }

    public final void startLoading() {
        this.mStarted = true;
        this.mReset = false;
        this.mAbandoned = false;
        onStartLoading();
    }

    public boolean cancelLoad() {
        return onCancelLoad();
    }

    public void stopLoading() {
        this.mStarted = false;
        onStopLoading();
    }

    public void abandon() {
        this.mAbandoned = true;
        onAbandon();
    }

    public void reset() {
        onReset();
        this.mReset = true;
        this.mStarted = false;
        this.mAbandoned = false;
        this.mContentChanged = false;
        this.mProcessingChange = false;
    }

    public String dataToString(D d) {
        StringBuilder stringBuilder = new StringBuilder(64);
        DebugUtils.buildShortClassTag(d, stringBuilder);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(64);
        DebugUtils.buildShortClassTag(this, stringBuilder);
        stringBuilder.append(" id=");
        stringBuilder.append(this.mId);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Deprecated
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print(str);
        printWriter.print("mId=");
        printWriter.print(this.mId);
        printWriter.print(" mListener=");
        printWriter.println(this.mListener);
        if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
            printWriter.print(str);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mContentChanged=");
            printWriter.print(this.mContentChanged);
            printWriter.print(" mProcessingChange=");
            printWriter.println(this.mProcessingChange);
        }
        if (this.mAbandoned || this.mReset) {
            printWriter.print(str);
            printWriter.print("mAbandoned=");
            printWriter.print(this.mAbandoned);
            printWriter.print(" mReset=");
            printWriter.println(this.mReset);
        }
    }
}
