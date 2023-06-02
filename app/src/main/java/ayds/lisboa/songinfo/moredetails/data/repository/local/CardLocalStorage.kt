package ayds.lisboa.songinfo.moredetails.data.repository.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 4
private const val SORT_ORDER = "card DESC"

interface CardLocalStorage {
    fun saveCard(cardData: CardData)
    fun getCards(card: String): List<CardData>
}

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToCardDataMapper: CursorToCardDataMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        CARD_COLUMN,
        DESCRIPTION_COLUMN,
        INFO_URL_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createCardTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CARD_TABLE")
        onCreate(db)
    }

    override fun saveCard(cardData: CardData) {
        val cardMap = getCardDataMap(cardData)

        writableDatabase.insert(CARD_TABLE, null, cardMap)
    }

    private fun getCardDataMap(card: CardData): ContentValues {
        val values = ContentValues()

        values.put(CARD_COLUMN, card.cardName)
        values.put(DESCRIPTION_COLUMN, card.description)
        values.put(INFO_URL_COLUMN, card.infoURL)
        values.put(SOURCE_COLUMN, card.source.ordinal)
        values.put(SOURCE_LOGO_COLUMN, card.sourceLogoURL)

        return values
    }

    override fun getCards(card: String): List<CardData> {
        val cardCursor = getCardCursor(card)
        val cardsData =  mutableListOf<CardData>()

        while(cardCursor.moveToNext()){
            val cardData = cursorToCardDataMapper.map(cardCursor)
            cardData?.let{cardsData.add(it)}
        }

        return cardsData
    }

    private fun getCardCursor(card: String): Cursor {
        return readableDatabase.query(
            CARD_TABLE,
            projection,
            "$CARD_COLUMN = ?",
            arrayOf(card),
            null,
            null,
            SORT_ORDER
        )
    }
}