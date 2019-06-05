// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.app.RemoteInput$Builder;
import android.os.Bundle;
import java.util.Set;

public final class RemoteInput
{
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;
    
    static android.app.RemoteInput fromCompat(final RemoteInput remoteInput) {
        return new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }
    
    static android.app.RemoteInput[] fromCompat(final RemoteInput[] array) {
        if (array == null) {
            return null;
        }
        final android.app.RemoteInput[] array2 = new android.app.RemoteInput[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = fromCompat(array[i]);
        }
        return array2;
    }
    
    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }
    
    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }
    
    public CharSequence[] getChoices() {
        return this.mChoices;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public CharSequence getLabel() {
        return this.mLabel;
    }
    
    public String getResultKey() {
        return this.mResultKey;
    }
}
