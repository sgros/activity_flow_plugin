// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

public class Const
{
    public static final int PRECISION = 5;
    private static final int PUBLISH_PRIVATE = 2;
    private static final int PUBLISH_RELEASE = 0;
    private static final int RELEASE = 2;
    public static final float RHO = 57.29578f;
    public static int SCREEN_HEIGHT = 0;
    public static final int SCREEN_SIZE_LARGE = 2;
    public static final int SCREEN_SIZE_MEDIUM = 1;
    public static final int SCREEN_SIZE_SMALL = 0;
    public static final int SCREEN_SIZE_XLARGE = 3;
    public static int SCREEN_WIDTH = 0;
    public static boolean STATE_DEBUG_LOGS = false;
    public static boolean STATE_RELEASE = false;
    public static final int TEXT_SIZE_BIG = 2;
    public static final int TEXT_SIZE_HUGE = 3;
    public static final int TEXT_SIZE_MEDIUM = 1;
    public static final int TEXT_SIZE_SMALL = 0;
    
    static {
        Const.SCREEN_WIDTH = 0;
        Const.SCREEN_HEIGHT = 0;
        switch (2) {
            case 0: {
                Const.STATE_RELEASE = true;
                Const.STATE_DEBUG_LOGS = false;
                break;
            }
            case 2: {
                Const.STATE_RELEASE = false;
                Const.STATE_DEBUG_LOGS = true;
                break;
            }
        }
    }
}
