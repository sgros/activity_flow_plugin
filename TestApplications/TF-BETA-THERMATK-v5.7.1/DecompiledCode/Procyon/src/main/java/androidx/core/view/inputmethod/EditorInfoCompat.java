// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view.inputmethod;

import android.os.Bundle;
import android.os.Build$VERSION;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat
{
    private static final String[] EMPTY_STRING_ARRAY;
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
    
    public static String[] getContentMimeTypes(final EditorInfo editorInfo) {
        if (Build$VERSION.SDK_INT >= 25) {
            String[] array = editorInfo.contentMimeTypes;
            if (array == null) {
                array = EditorInfoCompat.EMPTY_STRING_ARRAY;
            }
            return array;
        }
        final Bundle extras = editorInfo.extras;
        if (extras == null) {
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        String[] array2;
        if ((array2 = extras.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES")) == null) {
            array2 = editorInfo.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        }
        if (array2 == null) {
            array2 = EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        return array2;
    }
    
    public static void setContentMimeTypes(final EditorInfo editorInfo, final String[] contentMimeTypes) {
        if (Build$VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = contentMimeTypes;
        }
        else {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
            editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
        }
    }
}
