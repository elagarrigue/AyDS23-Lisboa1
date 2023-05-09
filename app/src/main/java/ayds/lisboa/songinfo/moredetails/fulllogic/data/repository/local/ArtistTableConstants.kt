package ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.local

private const val ARTIST_TABLE = "artists"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"

private const val createArtistTableQuery: String =
    "create table $ARTIST_TABLE " +
            "($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"