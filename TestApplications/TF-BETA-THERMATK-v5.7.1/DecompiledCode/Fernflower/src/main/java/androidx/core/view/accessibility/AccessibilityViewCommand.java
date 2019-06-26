package androidx.core.view.accessibility;

import android.os.Bundle;
import android.view.View;

public interface AccessibilityViewCommand {
   boolean perform(View var1, AccessibilityViewCommand.CommandArguments var2);

   public abstract static class CommandArguments {
      private static final Bundle sEmptyBundle = new Bundle();
      Bundle mBundle;

      public void setBundle(Bundle var1) {
         this.mBundle = var1;
      }
   }

   public static final class MoveAtGranularityArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class MoveHtmlArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class MoveWindowArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class ScrollToPositionArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class SetProgressArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class SetSelectionArguments extends AccessibilityViewCommand.CommandArguments {
   }

   public static final class SetTextArguments extends AccessibilityViewCommand.CommandArguments {
   }
}
