package org.mozilla.focus.urlinput;

import java.util.List;

public class UrlInputContract {

    interface Presenter {
        void onInput(CharSequence charSequence, boolean z);

        void setView(View view);
    }

    interface View {
        void setQuickSearchVisible(boolean z);

        void setSuggestions(List<CharSequence> list);
    }
}
