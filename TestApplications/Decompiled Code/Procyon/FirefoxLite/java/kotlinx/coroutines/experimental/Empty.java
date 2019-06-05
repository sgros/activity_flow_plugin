// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

final class Empty implements Incomplete
{
    private final boolean isActive;
    
    public Empty(final boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public NodeList getList() {
        return null;
    }
    
    @Override
    public boolean isActive() {
        return this.isActive;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Empty{");
        String str;
        if (this.isActive()) {
            str = "Active";
        }
        else {
            str = "New";
        }
        sb.append(str);
        sb.append('}');
        return sb.toString();
    }
}
