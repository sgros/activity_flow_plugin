package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.LocaleController;

// $FF: synthetic class
public final class _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU implements Comparator {
   // $FF: synthetic field
   private final LocaleController.LocaleInfo f$0;

   // $FF: synthetic method
   public _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU(LocaleController.LocaleInfo var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return LanguageSelectActivity.lambda$fillLanguages$3(this.f$0, (LocaleController.LocaleInfo)var1, (LocaleController.LocaleInfo)var2);
   }
}
