package androidx.work.impl.model;

public class SystemIdInfo {
   public final int systemId;
   public final String workSpecId;

   public SystemIdInfo(String var1, int var2) {
      this.workSpecId = var1;
      this.systemId = var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         SystemIdInfo var2 = (SystemIdInfo)var1;
         return this.systemId != var2.systemId ? false : this.workSpecId.equals(var2.workSpecId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.workSpecId.hashCode() * 31 + this.systemId;
   }
}
