package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "dictionary.db";
  private static final int DATABASE_VERSION = 1;

  private static final String ARTIST_TABLE = "artists";
  private static final String ID_COLUMN = "id";
  private static final String ARTIST_COLUMN = "artist";
  private static final String INFO_COLUMN = "info";
  private static final String SOURCE_COLUMN = "source";
  private static final String [] PROYECTION = {
          ID_COLUMN,
          ARTIST_COLUMN,
          INFO_COLUMN
  };

  public DataBase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
            "create table " + ARTIST_TABLE + " ( " + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ARTIST_COLUMN + " string, " + INFO_COLUMN + " string, " + SOURCE_COLUMN + " integer)");

    Log.i("DB", "DB created");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public  void saveArtist(String artist, String info) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues artistMap = getArtistInfoMap(artist, info);
    db.insert(ARTIST_TABLE, null, artistMap);
  }

  private ContentValues getArtistInfoMap(String artist, String info) {
    ContentValues values = new ContentValues();

    values.put(ARTIST_COLUMN, artist);
    values.put(INFO_COLUMN, info);
    values.put(SOURCE_COLUMN, 1);

    return values;
  }

  public String getInfo(String artist) {
    String artistInfo;
    String sortOrder = "artist DESC";

    Cursor artistsCursor = getArtistCursor(artist, sortOrder);
    List<String> artistsInfoList = getInfoFromCursor(artistsCursor);

    artistInfo = artistsInfoList.isEmpty()? null : artistsInfoList.get(0);
    return artistInfo;
  }

  private Cursor getArtistCursor(String artist, String sortOrder) {
    String[] selectionArgs = {artist};

    Cursor cursorQuery = getReadableDatabase().query(
            ARTIST_TABLE,
            PROYECTION,
            ARTIST_COLUMN + "= ?",
            selectionArgs,
            null,
            null,
            sortOrder
    );

    return cursorQuery;
  }

  private List<String> getInfoFromCursor(Cursor query) {
    List<String> artistsInfo = new ArrayList<String>();
    if(!query.isClosed()) {
      while(query.moveToNext()) {
        String info = query.getString(
                query.getColumnIndexOrThrow(INFO_COLUMN));
        artistsInfo.add(info);
      }
    }

    query.close();
    return artistsInfo;
  }
}
