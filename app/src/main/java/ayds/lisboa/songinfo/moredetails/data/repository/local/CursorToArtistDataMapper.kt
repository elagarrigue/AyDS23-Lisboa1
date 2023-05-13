package ayds.lisboa.songinfo.moredetails.data.repository.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import java.sql.SQLException

interface CursorToArtistDataMapper {
    fun map(cursor: Cursor): ArtistData?
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper {

    override fun map(cursor: Cursor): ArtistData? =
        try {
            with(cursor) {
                if(moveToNext()) {
                    ArtistData(
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        infoArtist = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        url = getString(getColumnIndexOrThrow(URL_COLUMN))
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