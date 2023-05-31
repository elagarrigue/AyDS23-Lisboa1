package ayds.lisboa.songinfo.moredetails.data.repository.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import java.sql.SQLException

interface CursorToArtistDataMapper {
    fun map(cursor: Cursor): CardData?
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper {

    override fun map(cursor: Cursor): CardData? =
        try {
            with(cursor) {
                if(moveToNext()) {
                    CardData(
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
                        infoURL = getString(getColumnIndexOrThrow(INFO_URL_COLUMN)),
                    )
                }
                else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }

}