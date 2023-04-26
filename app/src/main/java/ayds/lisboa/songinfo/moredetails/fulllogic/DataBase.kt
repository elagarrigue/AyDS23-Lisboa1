package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
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

  @Override
  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(createArtistTableQuery)
  }

  @Override
  override fun onUpgrade(db: SQLiteDatabase ,oldVersion: Int, newVersion: Int) {}

  fun saveArtist(artist: String, info: String) {
    val db: SQLiteDatabase = writableDatabase
    val artistMap = getArtistInfoMap(artist, info)

    db.insert(ARTIST_TABLE, null, artistMap)
  }

  private fun getArtistInfoMap(artist: String, info: String): ContentValues {
    val values = ContentValues()

    values.put(ARTIST_COLUMN, artist)
    values.put(INFO_COLUMN, info)
    values.put(SOURCE_COLUMN, 1)

    return values
  }

  fun getInfo(artist: String): String? {
    val artistInfo: String

    val artistsCursor = getArtistCursor(artist)
    val artistsInfoList = getInfoFromCursor(artistsCursor)


    return if(artistsInfoList.isEmpty()) null else artistsInfoList[0]
  }

  private fun getArtistCursor(artist: String): Cursor {
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

  private fun getInfoFromCursor(query: Cursor): List<String> {
    val artistsInfo: MutableList<String> = ArrayList()
    while(query.moveToNext()) {
      val info = query.getString(query.getColumnIndexOrThrow(INFO_COLUMN))
      artistsInfo.add(info)
    }

    return artistsInfo
  }
}