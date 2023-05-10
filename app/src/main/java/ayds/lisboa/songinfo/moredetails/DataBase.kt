package ayds.lisboa.songinfo.moredetails

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1

private const val ARTIST_TABLE = "artists"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"
private const val SORT_ORDER = "artist DESC"

private const val createArtistTableQuery: String =
  "create table $ARTIST_TABLE " +
        "($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$ARTIST_COLUMN string, " +
        "$INFO_COLUMN string, " +
        "$SOURCE_COLUMN integer)"

internal class DataBase(context: Context ) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  private val projection = arrayOf(
    ID_COLUMN,
    ARTIST_COLUMN,
    INFO_COLUMN
  )

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(createArtistTableQuery)
  }

  override fun onUpgrade(db: SQLiteDatabase ,oldVersion: Int, newVersion: Int) {}

  fun saveArtist(artist: String?, info: String?) {
    val artistMap = getArtistInfoMap(artist, info)

    writableDatabase.insert(ARTIST_TABLE, null, artistMap)
  }

  private fun getArtistInfoMap(artist: String?, info: String?): ContentValues {
    val values = ContentValues()

    values.put(ARTIST_COLUMN, artist)
    values.put(INFO_COLUMN, info)
    values.put(SOURCE_COLUMN, 1)

    return values
  }

  fun getInfo(artist: String?): String? {
    val artistCursor = getArtistCursor(artist)
    val artistInfoList = getInfoFromCursor(artistCursor)

    return artistInfoList.firstOrNull()
  }

  private fun getArtistCursor(artist: String?): Cursor {
    return readableDatabase.query(
      ARTIST_TABLE,
      projection,
      "$ARTIST_COLUMN = ?",
      arrayOf(artist),
      null,
      null,
      SORT_ORDER
    )
  }

  private fun getInfoFromCursor(artistCursor: Cursor): List<String> {
    val artistInfo: MutableList<String> = ArrayList()

    while(artistCursor.moveToNext()) {
      val info = artistCursor.getString(artistCursor.getColumnIndexOrThrow(INFO_COLUMN))
      artistInfo.add(info)
    }

    return artistInfo
  }
}