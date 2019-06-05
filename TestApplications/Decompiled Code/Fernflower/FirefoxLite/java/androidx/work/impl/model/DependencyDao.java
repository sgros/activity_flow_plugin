package androidx.work.impl.model;

import java.util.List;

public interface DependencyDao {
   List getDependentWorkIds(String var1);

   boolean hasCompletedAllPrerequisites(String var1);

   boolean hasDependents(String var1);

   void insertDependency(Dependency var1);
}
