package androidx.work.impl.model;

import java.util.List;

public interface WorkTagDao {
   List getTagsForWorkSpecId(String var1);

   void insert(WorkTag var1);
}
