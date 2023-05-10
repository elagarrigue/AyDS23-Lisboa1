package ayds.lisboa.songinfo.moredetails.data.repository.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val SORT_ORDER = "artist DESC"

interface ArtistLocalStorage {
    fun saveArtist(artist: ArtistData)
    fun getArtist(artist: String): ArtistData?
}

internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToArtistDataMapper: CursorToArtistDataMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        URL_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: ArtistData) {
        val artistMap = getArtistInfoMap(artist)

        writableDatabase.insert(ARTIST_TABLE, null, artistMap)
    }

    private fun getArtistInfoMap(artist: ArtistData): ContentValues {
        val values = ContentValues()

        values.put(ARTIST_COLUMN, artist.artistName)
        values.put(INFO_COLUMN, artist.infoArtist)
        values.put(URL_COLUMN, artist.url)
        values.put(SOURCE_COLUMN, 1)

        return values
    }

    override fun getArtist(artist: String): ArtistData? {
        val artistCursor = getArtistCursor(artist)

        return cursorToArtistDataMapper.map(artistCursor)
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
}