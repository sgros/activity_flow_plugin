package android.arch.persistence.room;

public class RoomMasterTable {
    public static String createInsertQuery(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"");
        stringBuilder.append(str);
        stringBuilder.append("\")");
        return stringBuilder.toString();
    }
}
