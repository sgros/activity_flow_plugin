// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

public enum ID3v1Genre
{
    ACapella("A capella"), 
    Acid("Acid"), 
    AcidJazz("Acid Jazz"), 
    AcidPunk("Acid Punk"), 
    Acoustic("Acoustic"), 
    AlternRock("AlternRock"), 
    Alternative("Alternative"), 
    Ambient("Ambient"), 
    Avantgarde("Avantgarde"), 
    Ballad("Ballad"), 
    Bass("Bass"), 
    Bebop("Bebop"), 
    BigBand("Big Band"), 
    Bluegrass("Bluegrass"), 
    Blues("Blues"), 
    BootyBass("Booty Bass"), 
    Cabaret("Cabaret"), 
    Celtic("Celtic"), 
    ChamberMusic("Chamber Music"), 
    Chanson("Chanson"), 
    Chorus("Chorus"), 
    ChristianRap("Christian Rap"), 
    ClassicRock("Classic Rock"), 
    Classical("Classical"), 
    Club("Club"), 
    Comedy("Comedy"), 
    Country("Country"), 
    Cult("Cult"), 
    Dance("Dance"), 
    DanceHall("Dance Hall"), 
    Darkwave("Darkwave"), 
    DeathMetal("Death Metal"), 
    Disco("Disco"), 
    Dream("Dream"), 
    DrumSolo("Drum Solo"), 
    Duet("Duet"), 
    EasyListening("Easy Listening"), 
    Electronic("Electronic"), 
    Ethnic("Ethnic"), 
    EuroHouse("Euro-House"), 
    EuroTechno("Euro-Techno"), 
    Eurodance("Eurodance"), 
    FastFusion("Fast Fusion"), 
    Folk("Folk"), 
    FolkRock("Folk-Rock"), 
    Folklore("Folklore"), 
    Freestyle("Freestyle"), 
    Funk("Funk"), 
    Fusion("Fusion"), 
    Game("Game"), 
    Gangsta("Gangsta"), 
    Gospel("Gospel"), 
    Gothic("Gothic"), 
    GothicRock("Gothic Rock"), 
    Grunge("Grunge"), 
    HardRock("Hard Rock"), 
    HipHop("Hip-Hop"), 
    House("House"), 
    Humour("Humour"), 
    Industrial("Industrial"), 
    Instrumental("Instrumental"), 
    InstrumentalPop("Instrumental Pop"), 
    InstrumentalRock("Instrumental Rock"), 
    Jazz("Jazz"), 
    JazzFunk("Jazz+Funk"), 
    Jungle("Jungle"), 
    Latin("Latin"), 
    LoFi("Lo-Fi"), 
    Meditative("Meditative"), 
    Metal("Metal"), 
    Musical("Musical"), 
    NationalFolk("National Folk"), 
    NativeAmerican("Native American"), 
    NewAge("New Age"), 
    NewWave("New Wave"), 
    Noise("Noise"), 
    Oldies("Oldies"), 
    Opera("Opera"), 
    Other("Other"), 
    Polka("Polka"), 
    Pop("Pop"), 
    PopFolk("Pop-Folk"), 
    PopFunk("Pop/Funk"), 
    PornGroove("Porn Groove"), 
    PowerBallad("Power Ballad"), 
    Pranks("Pranks"), 
    Primus("Primus"), 
    ProgressiveRock("Progressive Rock"), 
    Psychadelic("Psychadelic"), 
    PsychedelicRock("Psychedelic Rock"), 
    Punk("Punk"), 
    PunkRock("Punk Rock"), 
    Rap("Rap"), 
    Rave("Rave"), 
    Reggae("Reggae"), 
    Retro("Retro"), 
    Revival("Revival"), 
    RhytmicSoul("Rhythmic Soul"), 
    RnB("R&B"), 
    Rock("Rock"), 
    RockAndRoll("Rock & Roll"), 
    Samba("Samba"), 
    Satire("Satire"), 
    Showtunes("Showtunes"), 
    Ska("Ska"), 
    SlowJam("Slow Jam"), 
    SlowRock("Slow Rock"), 
    Sonata("Sonata"), 
    Soul("Soul"), 
    SoundClip("Sound Clip"), 
    Soundtrack("Soundtrack"), 
    SouthernRock("Southern Rock"), 
    Space("Space"), 
    Speech("Speech"), 
    Swing("Swing"), 
    SymphonicRock("Symphonic Rock"), 
    Symphony("Symphony"), 
    Tango("Tango"), 
    Techno("Techno"), 
    TechnoIndustrial("Techno-Industrial"), 
    Top40("Top 40"), 
    Trailer("Trailer"), 
    Trance("Trance"), 
    Tribal("Tribal"), 
    TripHop("Trip-Hop"), 
    Vocal("Vocal");
    
    private final String description;
    
    private ID3v1Genre(final String description) {
        this.description = description;
    }
    
    public static ID3v1Genre getGenre(final int n) {
        final ID3v1Genre[] values = values();
        ID3v1Genre id3v1Genre;
        if (n >= 0 && n < values.length) {
            id3v1Genre = values[n];
        }
        else {
            id3v1Genre = null;
        }
        return id3v1Genre;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public int getId() {
        return this.ordinal();
    }
}
