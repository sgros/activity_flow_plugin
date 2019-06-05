package org.mozilla.focus.urlinput;

import java.util.List;

public class UrlInputContract {
   interface Presenter {
      void onInput(CharSequence var1, boolean var2);

      void setView(UrlInputContract.View var1);
   }

   interface View {
      void setQuickSearchVisible(boolean var1);

      void setSuggestions(List var1);
   }
}
