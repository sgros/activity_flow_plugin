package okio;

final class Segment {
   static final int SHARE_MINIMUM = 1024;
   static final int SIZE = 8192;
   final byte[] data;
   int limit;
   Segment next;
   boolean owner;
   int pos;
   Segment prev;
   boolean shared;

   Segment() {
      this.data = new byte[8192];
      this.owner = true;
      this.shared = false;
   }

   Segment(Segment var1) {
      this(var1.data, var1.pos, var1.limit);
      var1.shared = true;
   }

   Segment(byte[] var1, int var2, int var3) {
      this.data = var1;
      this.pos = var2;
      this.limit = var3;
      this.owner = false;
      this.shared = true;
   }

   public void compact() {
      if (this.prev == this) {
         throw new IllegalStateException();
      } else {
         if (this.prev.owner) {
            int var1 = this.limit - this.pos;
            int var2 = this.prev.limit;
            int var3;
            if (this.prev.shared) {
               var3 = 0;
            } else {
               var3 = this.prev.pos;
            }

            if (var1 <= 8192 - var2 + var3) {
               this.writeTo(this.prev, var1);
               this.pop();
               SegmentPool.recycle(this);
            }
         }

      }
   }

   public Segment pop() {
      Segment var1;
      if (this.next != this) {
         var1 = this.next;
      } else {
         var1 = null;
      }

      this.prev.next = this.next;
      this.next.prev = this.prev;
      this.next = null;
      this.prev = null;
      return var1;
   }

   public Segment push(Segment var1) {
      var1.prev = this;
      var1.next = this.next;
      this.next.prev = var1;
      this.next = var1;
      return var1;
   }

   public Segment split(int var1) {
      if (var1 > 0 && var1 <= this.limit - this.pos) {
         Segment var2;
         if (var1 >= 1024) {
            var2 = new Segment(this);
         } else {
            var2 = SegmentPool.take();
            System.arraycopy(this.data, this.pos, var2.data, 0, var1);
         }

         var2.limit = var2.pos + var1;
         this.pos += var1;
         this.prev.push(var2);
         return var2;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void writeTo(Segment var1, int var2) {
      if (!var1.owner) {
         throw new IllegalArgumentException();
      } else {
         if (var1.limit + var2 > 8192) {
            if (var1.shared) {
               throw new IllegalArgumentException();
            }

            if (var1.limit + var2 - var1.pos > 8192) {
               throw new IllegalArgumentException();
            }

            System.arraycopy(var1.data, var1.pos, var1.data, 0, var1.limit - var1.pos);
            var1.limit -= var1.pos;
            var1.pos = 0;
         }

         System.arraycopy(this.data, this.pos, var1.data, var1.limit, var2);
         var1.limit += var2;
         this.pos += var2;
      }
   }
}
