package menion.android.whereyougo.guide;

import android.net.Uri;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.audio.AudioClip;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;

public class Guide implements IGuide {
    private static final String TAG = "WaypointGuide";
    private boolean alreadyBeeped = false;
    private AudioClip audioBeep;
    private float azimuth;
    private float distance;
    /* renamed from: id */
    private int f82id;
    private long lastSonarCall = 0;
    private final Location location;
    private final String name;

    public Guide(String name, Location location) {
        this.name = name;
        this.location = location;
        try {
            this.audioBeep = new AudioClip(C0322A.getApp(), (int) C0254R.raw.sound_beep_01);
        } catch (Exception e) {
            Logger.m21e(TAG, "Guide(2131099648), e:" + e.toString());
        }
    }

    public void actualizeState(Location actualLocation) {
        this.azimuth = actualLocation.bearingTo(this.location);
        this.distance = actualLocation.distanceTo(this.location);
    }

    public float getAzimuthToTaget() {
        return this.azimuth;
    }

    public float getDistanceToTarget() {
        return this.distance;
    }

    public int getId() {
        return this.f82id;
    }

    private long getSonarTimeout(double distance) {
        if (distance < ((double) Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE)) {
            return (long) ((1000.0d * distance) / 33.0d);
        }
        return Long.MAX_VALUE;
    }

    public Location getTargetLocation() {
        return this.location;
    }

    public String getTargetName() {
        return this.name;
    }

    public long getTimeToTarget() {
        if (((double) LocationState.getLocation().getSpeed()) > 1.0d) {
            return (long) ((getDistanceToTarget() / LocationState.getLocation().getSpeed()) * 1000.0f);
        }
        return 0;
    }

    public void manageDistanceSoundsBeeping(double distance) {
        try {
            switch (Preferences.GUIDING_WAYPOINT_SOUND) {
                case 0:
                    long currentTime = System.currentTimeMillis();
                    if (((float) (currentTime - this.lastSonarCall)) > ((float) getSonarTimeout(distance))) {
                        this.lastSonarCall = currentTime;
                        playSingleBeep();
                        return;
                    }
                    return;
                case 1:
                    if (distance < ((double) Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE) && !this.alreadyBeeped) {
                        playSingleBeep();
                        this.alreadyBeeped = true;
                        return;
                    }
                    return;
                case 2:
                    if (distance < ((double) Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE) && !this.alreadyBeeped) {
                        playCustomSound();
                        this.alreadyBeeped = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            Logger.m21e(TAG, "manageDistanceSounds(" + distance + "), e:" + e.toString());
        }
        Logger.m21e(TAG, "manageDistanceSounds(" + distance + "), e:" + e.toString());
    }

    /* Access modifiers changed, original: protected */
    public void playCustomSound() {
        String uri = Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI;
        try {
            final AudioClip audioClip = new AudioClip(C0322A.getApp(), Uri.parse(uri));
            audioClip.play();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    AudioClip.destroyAudio(audioClip);
                }
            }, 5000);
        } catch (Exception e) {
            Logger.m21e(TAG, "playCustomSound(" + uri + "), e:" + e.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void playSingleBeep() {
        if (this.audioBeep != null) {
            this.audioBeep.play();
        }
    }
}
