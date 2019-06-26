package android.support.constraint.solver.widgets;

public class Rectangle {
   public int height;
   public int width;
   public int x;
   public int y;

   public boolean contains(int var1, int var2) {
      boolean var3;
      if (var1 >= this.x && var1 < this.x + this.width && var2 >= this.y && var2 < this.y + this.height) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public int getCenterX() {
      return (this.x + this.width) / 2;
   }

   public int getCenterY() {
      return (this.y + this.height) / 2;
   }

   void grow(int var1, int var2) {
      this.x -= var1;
      this.y -= var2;
      this.width += var1 * 2;
      this.height += 2 * var2;
   }

   boolean intersects(Rectangle var1) {
      boolean var2;
      if (this.x >= var1.x && this.x < var1.x + var1.width && this.y >= var1.y && this.y < var1.y + var1.height) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }
}
