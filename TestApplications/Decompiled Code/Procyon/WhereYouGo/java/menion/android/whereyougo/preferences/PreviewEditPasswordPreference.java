// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import android.util.AttributeSet;
import android.content.Context;

public class PreviewEditPasswordPreference extends PreviewEditTextPreference
{
    protected CharSequence previewTemplate;
    protected CharSequence summaryTemplate;
    
    public PreviewEditPasswordPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.summaryTemplate = "";
        this.previewTemplate = "";
    }
    
    @Override
    public CharSequence getSummary() {
        final String string = this.previewTemplate.toString();
        final String text = this.getText();
        String string2 = "";
        for (int i = 0; i < text.length(); ++i) {
            string2 += "\u2022";
        }
        String str;
        if (string.length() == 0) {
            str = "(" + string2 + ")";
        }
        else {
            str = string.replace("%1$", string2);
        }
        return str + " " + (Object)this.summaryTemplate;
    }
}
