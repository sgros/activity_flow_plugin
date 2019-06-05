package android.support.transition;

import android.view.View;
import android.view.ViewGroup;

public class Scene {
   private Runnable mExitAction;
   private ViewGroup mSceneRoot;

   static Scene getCurrentScene(View var0) {
      return (Scene)var0.getTag(R.id.transition_current_scene);
   }

   static void setCurrentScene(View var0, Scene var1) {
      var0.setTag(R.id.transition_current_scene, var1);
   }

   public void exit() {
      if (getCurrentScene(this.mSceneRoot) == this && this.mExitAction != null) {
         this.mExitAction.run();
      }

   }
}
