package android.support.constraint.solver.widgets;

import java.util.HashSet;
import java.util.Iterator;

public class ResolutionNode {
   HashSet dependents = new HashSet(2);
   int state = 0;

   public void addDependent(ResolutionNode var1) {
      this.dependents.add(var1);
   }

   public void didResolve() {
      this.state = 1;
      Iterator var1 = this.dependents.iterator();

      while(var1.hasNext()) {
         ((ResolutionNode)var1.next()).resolve();
      }

   }

   public void invalidate() {
      this.state = 0;
      Iterator var1 = this.dependents.iterator();

      while(var1.hasNext()) {
         ((ResolutionNode)var1.next()).invalidate();
      }

   }

   public boolean isResolved() {
      int var1 = this.state;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public void reset() {
      this.state = 0;
      this.dependents.clear();
   }

   public void resolve() {
   }
}
