package androidx.work.impl.model;

public class Dependency {
   public final String prerequisiteId;
   public final String workSpecId;

   public Dependency(String var1, String var2) {
      this.workSpecId = var1;
      this.prerequisiteId = var2;
   }
}
