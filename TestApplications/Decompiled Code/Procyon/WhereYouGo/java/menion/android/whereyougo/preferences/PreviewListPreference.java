// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import android.util.AttributeSet;
import android.content.Context;
import android.preference.ListPreference;

public class PreviewListPreference extends ListPreference
{
    protected CharSequence previewTemplate;
    protected CharSequence summaryTemplate;
    
    public PreviewListPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.summaryTemplate = "";
        this.previewTemplate = "";
        this.summaryTemplate = super.getSummary();
        for (int i = 0; i < set.getAttributeCount(); ++i) {
            final String attributeName = set.getAttributeName(i);
            final String attributeValue = set.getAttributeValue(i);
            if (attributeName.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = attributeValue;
            }
        }
    }
    
    public CharSequence getSummary() {
        final String string = this.previewTemplate.toString();
        String str;
        if (string.length() == 0) {
            str = "(" + (Object)this.getEntry() + ")";
        }
        else {
            str = string.replace("%1$", this.getEntry());
        }
        return str + " " + (Object)this.summaryTemplate;
    }
    
    protected void onDialogClosed(final boolean b) {
        super.onDialogClosed(b);
        this.setSummary(this.getSummary());
    }
}
