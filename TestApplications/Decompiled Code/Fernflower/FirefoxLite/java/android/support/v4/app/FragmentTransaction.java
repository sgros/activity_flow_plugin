package android.support.v4.app;

public abstract class FragmentTransaction {
   public abstract FragmentTransaction add(int var1, Fragment var2, String var3);

   public abstract FragmentTransaction add(Fragment var1, String var2);

   public abstract FragmentTransaction addToBackStack(String var1);

   public abstract int commit();

   public abstract int commitAllowingStateLoss();

   public abstract FragmentTransaction remove(Fragment var1);

   public abstract FragmentTransaction replace(int var1, Fragment var2);

   public abstract FragmentTransaction replace(int var1, Fragment var2, String var3);

   public abstract FragmentTransaction setCustomAnimations(int var1, int var2, int var3, int var4);
}
