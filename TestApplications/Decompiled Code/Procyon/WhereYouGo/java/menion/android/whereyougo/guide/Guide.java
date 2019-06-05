// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.guide;

import java.util.TimerTask;
import java.util.Timer;
import android.net.Uri;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;
import android.content.Context;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.audio.AudioClip;

public class Guide implements IGuide
{
    private static final String TAG = "WaypointGuide";
    private boolean alreadyBeeped;
    private AudioClip audioBeep;
    private float azimuth;
    private float distance;
    private int id;
    private long lastSonarCall;
    private final Location location;
    private final String name;
    
    public Guide(final String name, final Location location) {
        this.name = name;
        this.location = location;
        this.alreadyBeeped = false;
        this.lastSonarCall = 0L;
        try {
            this.audioBeep = new AudioClip((Context)A.getApp(), 2131099648);
        }
        catch (Exception ex) {
            Logger.e("WaypointGuide", "Guide(2131099648), e:" + ex.toString());
        }
    }
    
    private long getSonarTimeout(final double n) {
        long n2;
        if (n < Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE) {
            n2 = (long)(1000.0 * n / 33.0);
        }
        else {
            n2 = Long.MAX_VALUE;
        }
        return n2;
    }
    
    @Override
    public void actualizeState(final Location location) {
        this.azimuth = location.bearingTo(this.location);
        this.distance = location.distanceTo(this.location);
    }
    
    @Override
    public float getAzimuthToTaget() {
        return this.azimuth;
    }
    
    @Override
    public float getDistanceToTarget() {
        return this.distance;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Override
    public Location getTargetLocation() {
        return this.location;
    }
    
    @Override
    public String getTargetName() {
        return this.name;
    }
    
    @Override
    public long getTimeToTarget() {
        long n;
        if (LocationState.getLocation().getSpeed() > 1.0) {
            n = (long)(this.getDistanceToTarget() / LocationState.getLocation().getSpeed() * 1000.0f);
        }
        else {
            n = 0L;
        }
        return n;
    }
    
    @Override
    public void manageDistanceSoundsBeeping(final double d) {
        while (true) {
            Label_0137: {
                Label_0097: {
                    try {
                        switch (Preferences.GUIDING_WAYPOINT_SOUND) {
                            case 1: {
                                if (d < Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE && !this.alreadyBeeped) {
                                    this.playSingleBeep();
                                    this.alreadyBeeped = true;
                                    break;
                                }
                                break;
                            }
                            case 0: {
                                break Label_0097;
                            }
                            case 2: {
                                break Label_0137;
                            }
                        }
                        return;
                    }
                    catch (Exception ex) {
                        Logger.e("WaypointGuide", "manageDistanceSounds(" + d + "), e:" + ex.toString());
                        return;
                    }
                }
                final long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.lastSonarCall > (float)this.getSonarTimeout(d)) {
                    this.lastSonarCall = currentTimeMillis;
                    this.playSingleBeep();
                    return;
                }
                return;
            }
            if (d < Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE && !this.alreadyBeeped) {
                this.playCustomSound();
                this.alreadyBeeped = true;
            }
        }
    }
    
    protected void playCustomSound() {
        final String guiding_WAYPOINT_SOUND_CUSTOM_SOUND_URI = Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI;
        try {
            final AudioClip audioClip = new AudioClip((Context)A.getApp(), Uri.parse(guiding_WAYPOINT_SOUND_CUSTOM_SOUND_URI));
            audioClip.play();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    AudioClip.destroyAudio(audioClip);
                }
            }, 5000L);
        }
        catch (Exception ex) {
            Logger.e("WaypointGuide", "playCustomSound(" + guiding_WAYPOINT_SOUND_CUSTOM_SOUND_URI + "), e:" + ex.toString());
        }
    }
    
    protected void playSingleBeep() {
        if (this.audioBeep != null) {
            this.audioBeep.play();
        }
    }
}
