package ayds.lisboa.songinfo.moredetails.data.repository.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 2
private const val SORT_ORDER = "artist DESC"

interface ArtistLocalStorage {
    fun saveArtist(artist: CardData)
    fun getArtist(artist: String): List<CardData>?
}

internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToArtistDataMapper: CursorToArtistDataMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        DESCRIPTION_COLUMN,
        INFO_URL_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $ARTIST_TABLE")
        onCreate(db)
    }

    override fun saveArtist(artist: CardData) {
        val artistMap = getArtistDataMap(artist)

        writableDatabase.insert(ARTIST_TABLE, null, artistMap)
    }

    private fun getArtistDataMap(artist: CardData): ContentValues {
        val values = ContentValues()

        values.put(ARTIST_COLUMN, artist.artistName)
        values.put(DESCRIPTION_COLUMN, artist.description)
        values.put(INFO_URL_COLUMN, artist.infoURL)
        values.put(SOURCE_COLUMN, artist.source.ordinal)
        values.put(SOURCE_LOGO_COLUMN, artist.sourceLogoURL)

        return values
    }

    override fun getArtist(artist: String): List<CardData> {
        val artistCursor = getArtistCursor(artist)
        val artistCards =  mutableListOf<CardData>()

        while(artistCursor.moveToNext()){
            val artistCardData = cursorToArtistDataMapper.map(artistCursor)
            artistCardData?.let{artistCards.add(it)}
        }
        return artistCards
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