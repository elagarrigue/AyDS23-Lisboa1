package ayds.lisboa.songinfo.moredetails.data.repository.local

const val CARD_TABLE = "cards"
const val ID_COLUMN = "id"
const val CARD_COLUMN = "card"
const val DESCRIPTION_COLUMN = "description"
const val INFO_URL_COLUMN = "url"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_COLUMN = "sourceLogo"

const val createCardTableQuery: String =
    "create table $CARD_TABLE " +
            "($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$CARD_COLUMN string, " +
            "$DESCRIPTION_COLUMN string, " +
            "$INFO_URL_COLUMN string, " +
            "$SOURCE_COLUMN integer, " +
            "$SOURCE_LOGO_COLUMN string)"