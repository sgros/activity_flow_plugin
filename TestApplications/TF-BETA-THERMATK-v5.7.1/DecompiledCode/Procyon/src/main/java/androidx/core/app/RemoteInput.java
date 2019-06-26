// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.app;

import java.util.HashSet;
import android.os.Build$VERSION;
import android.content.ClipDescription;
import android.content.ClipData;
import android.content.Intent;
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
    
    RemoteInput(final String mResultKey, final CharSequence mLabel, final CharSequence[] mChoices, final boolean mAllowFreeFormTextInput, final Bundle mExtras, final Set<String> mAllowedDataTypes) {
        this.mResultKey = mResultKey;
        this.mLabel = mLabel;
        this.mChoices = mChoices;
        this.mAllowFreeFormTextInput = mAllowFreeFormTextInput;
        this.mExtras = mExtras;
        this.mAllowedDataTypes = mAllowedDataTypes;
    }
    
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
    
    private static Intent getClipDataIntentFromIntent(final Intent intent) {
        final ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        final ClipDescription description = clipData.getDescription();
        if (!description.hasMimeType("text/vnd.android.intent")) {
            return null;
        }
        if (!description.getLabel().equals("android.remoteinput.results")) {
            return null;
        }
        return clipData.getItemAt(0).getIntent();
    }
    
    public static Bundle getResultsFromIntent(Intent clipDataIntentFromIntent) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 20) {
            return android.app.RemoteInput.getResultsFromIntent(clipDataIntentFromIntent);
        }
        if (sdk_INT < 16) {
            return null;
        }
        clipDataIntentFromIntent = getClipDataIntentFromIntent(clipDataIntentFromIntent);
        if (clipDataIntentFromIntent == null) {
            return null;
        }
        return (Bundle)clipDataIntentFromIntent.getExtras().getParcelable("android.remoteinput.resultsData");
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
    
    public boolean isDataOnly() {
        return !this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty();
    }
    
    public static final class Builder
    {
        private boolean mAllowFreeFormTextInput;
        private final Set<String> mAllowedDataTypes;
        private CharSequence[] mChoices;
        private final Bundle mExtras;
        private CharSequence mLabel;
        private final String mResultKey;
        
        public Builder(final String mResultKey) {
            this.mAllowedDataTypes = new HashSet<String>();
            this.mExtras = new Bundle();
            this.mAllowFreeFormTextInput = true;
            if (mResultKey != null) {
                this.mResultKey = mResultKey;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }
        
        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
        }
        
        public Builder setLabel(final CharSequence mLabel) {
            this.mLabel = mLabel;
            return this;
        }
    }
}
