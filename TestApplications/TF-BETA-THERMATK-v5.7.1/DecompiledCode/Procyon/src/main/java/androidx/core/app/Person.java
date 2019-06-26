// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.app;

import android.os.Bundle;
import android.graphics.drawable.Icon;
import android.app.Person$Builder;
import androidx.core.graphics.drawable.IconCompat;

public class Person
{
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;
    
    Person(final Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }
    
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    public String getKey() {
        return this.mKey;
    }
    
    public CharSequence getName() {
        return this.mName;
    }
    
    public String getUri() {
        return this.mUri;
    }
    
    public boolean isBot() {
        return this.mIsBot;
    }
    
    public boolean isImportant() {
        return this.mIsImportant;
    }
    
    public android.app.Person toAndroidPerson() {
        final Person$Builder setName = new Person$Builder().setName(this.getName());
        Icon icon;
        if (this.getIcon() != null) {
            icon = this.getIcon().toIcon();
        }
        else {
            icon = null;
        }
        return setName.setIcon(icon).setUri(this.getUri()).setKey(this.getKey()).setBot(this.isBot()).setImportant(this.isImportant()).build();
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putCharSequence("name", this.mName);
        final IconCompat mIcon = this.mIcon;
        Bundle bundle2;
        if (mIcon != null) {
            bundle2 = mIcon.toBundle();
        }
        else {
            bundle2 = null;
        }
        bundle.putBundle("icon", bundle2);
        bundle.putString("uri", this.mUri);
        bundle.putString("key", this.mKey);
        bundle.putBoolean("isBot", this.mIsBot);
        bundle.putBoolean("isImportant", this.mIsImportant);
        return bundle;
    }
    
    public static class Builder
    {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;
        
        public Person build() {
            return new Person(this);
        }
        
        public Builder setIcon(final IconCompat mIcon) {
            this.mIcon = mIcon;
            return this;
        }
        
        public Builder setName(final CharSequence mName) {
            this.mName = mName;
            return this;
        }
    }
}
