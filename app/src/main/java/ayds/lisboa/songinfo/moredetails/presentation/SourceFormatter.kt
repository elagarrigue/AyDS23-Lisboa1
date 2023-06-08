package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.Source

private const val SOURCE_STRING = "Fuente: "

private const val LASTFM = "LastFM"

private const val NYTIMES = "New York Times"

private const val WIKIPEDIA = "Wikipedia"

interface SourceFactory{
    fun getSourceString(source: Source) : String
}

internal class SourceFactoryImpl : SourceFactory{
    override fun getSourceString(source: Source) : String{
        return SOURCE_STRING +
                when(source) {
                    Source.LastFM -> LASTFM
                    Source.NYTimes -> NYTIMES
                    Source.Wikipedia -> WIKIPEDIA
                }
    }
}