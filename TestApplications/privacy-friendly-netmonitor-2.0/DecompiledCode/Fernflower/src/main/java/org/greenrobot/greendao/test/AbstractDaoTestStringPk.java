package org.greenrobot.greendao.test;

public abstract class AbstractDaoTestStringPk extends AbstractDaoTestSinglePk {
   public AbstractDaoTestStringPk(Class var1) {
      super(var1);
   }

   protected String createRandomPk() {
      int var1 = this.random.nextInt(30);
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < 1 + var1; ++var3) {
         var2.append((char)(97 + this.random.nextInt(25)));
      }

      return var2.toString();
   }
}
