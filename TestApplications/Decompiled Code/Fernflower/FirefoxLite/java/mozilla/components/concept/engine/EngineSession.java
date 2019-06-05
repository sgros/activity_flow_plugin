package mozilla.components.concept.engine;

import mozilla.components.support.base.observer.Observable;

public abstract class EngineSession implements Observable {
   public interface Observer {
      void onExternalResource(String var1, String var2, Long var3, String var4, String var5, String var6);

      void onFindResult(int var1, int var2, boolean var3);

      void onLoadingStateChange(boolean var1);

      void onLocationChange(String var1);

      void onNavigationStateChange(Boolean var1, Boolean var2);

      void onProgress(int var1);

      void onSecurityChange(boolean var1, String var2, String var3);

      void onTitleChange(String var1);

      public static final class DefaultImpls {
      }
   }
}
