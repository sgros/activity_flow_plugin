package androidx.work;

import androidx.work.impl.model.WorkSpec;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class WorkRequest {
   private UUID mId;
   private Set mTags;
   private WorkSpec mWorkSpec;

   protected WorkRequest(UUID var1, WorkSpec var2, Set var3) {
      this.mId = var1;
      this.mWorkSpec = var2;
      this.mTags = var3;
   }

   public String getStringId() {
      return this.mId.toString();
   }

   public Set getTags() {
      return this.mTags;
   }

   public WorkSpec getWorkSpec() {
      return this.mWorkSpec;
   }

   public abstract static class Builder {
      boolean mBackoffCriteriaSet = false;
      UUID mId = UUID.randomUUID();
      Set mTags = new HashSet();
      WorkSpec mWorkSpec;

      Builder(Class var1) {
         this.mWorkSpec = new WorkSpec(this.mId.toString(), var1.getName());
         this.addTag(var1.getName());
      }

      public final WorkRequest.Builder addTag(String var1) {
         this.mTags.add(var1);
         return this.getThis();
      }

      public final WorkRequest build() {
         WorkRequest var1 = this.buildInternal();
         this.mId = UUID.randomUUID();
         this.mWorkSpec = new WorkSpec(this.mWorkSpec);
         this.mWorkSpec.id = this.mId.toString();
         return var1;
      }

      abstract WorkRequest buildInternal();

      abstract WorkRequest.Builder getThis();
   }
}
