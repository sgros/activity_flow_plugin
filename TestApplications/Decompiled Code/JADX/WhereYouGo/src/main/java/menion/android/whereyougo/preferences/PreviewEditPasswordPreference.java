package menion.android.whereyougo.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class PreviewEditPasswordPreference extends PreviewEditTextPreference {
    protected CharSequence previewTemplate = "";
    protected CharSequence summaryTemplate = "";

    public PreviewEditPasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharSequence getSummary() {
        String preview = this.previewTemplate.toString();
        String value = getText();
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            newValue = newValue + "•";
        }
        if (preview.length() == 0) {
            preview = "(" + newValue + ")";
        } else {
            preview = preview.replace("%1$", newValue);
        }
        return preview + " " + this.summaryTemplate;
    }
}
