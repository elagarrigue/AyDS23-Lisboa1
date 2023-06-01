package ayds.lisboa.songinfo.moredetails.data.repository.local

const val ARTIST_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val DESCRIPTION_COLUMN = "description"
const val INFO_URL_COLUMN = "url"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_COLUMN = "sourceLogo"

const val createArtistTableQuery: String =
    "create table $ARTIST_TABLE " +
            "($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$DESCRIPTION_COLUMN string, " +
            "$INFO_URL_COLUMN string, " +
            "$SOURCE_COLUMN integer, " +
            "$SOURCE_LOGO_COLUMN string)"