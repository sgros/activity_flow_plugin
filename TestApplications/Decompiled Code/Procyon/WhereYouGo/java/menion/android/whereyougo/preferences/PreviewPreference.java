// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.Preference;

public class PreviewPreference extends Preference
{
    protected CharSequence defaultValue;
    protected String mValue;
    protected CharSequence previewTemplate;
    protected CharSequence summaryTemplate;
    
    public PreviewPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.summaryTemplate = "";
        this.previewTemplate = "";
        this.defaultValue = "";
        this.mValue = "";
        this.summaryTemplate = super.getSummary();
        for (int i = 0; i < set.getAttributeCount(); ++i) {
            final String attributeName = set.getAttributeName(i);
            final String attributeValue = set.getAttributeValue(i);
            if (attributeName.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = attributeValue;
            }
            if (attributeName.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = attributeValue;
            }
            if (attributeName.equalsIgnoreCase("defaultValue")) {
                this.defaultValue = attributeValue;
            }
        }
    }
    
    public CharSequence getSummary() {
        final String string = this.previewTemplate.toString();
        String str;
        if (string.length() == 0) {
            str = "(" + this.mValue + ")";
        }
        else {
            str = string.replace("%1$", this.mValue);
        }
        return str + " " + (Object)this.summaryTemplate;
    }
    
    protected Object onGetDefaultValue(final TypedArray typedArray, final int n) {
        return typedArray.getString(n);
    }
    
    protected void onSetInitialValue(final boolean b, final Object o) {
        String persistedString;
        if (b) {
            persistedString = this.getPersistedString((String)o);
        }
        else {
            persistedString = (String)o;
        }
        this.setValue(persistedString);
    }
    
    public void setValue(final String mValue) {
        this.persistString(this.mValue = mValue);
        this.notifyChanged();
    }
}
