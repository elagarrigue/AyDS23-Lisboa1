package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.Source

private const val SOURCE_STRING = "Fuente: "

interface SourceFactory{
    fun getSourceWrapper(source: Source) : SourceWrapper
}

internal class SourceFactoryImpl : SourceFactory{
    private val lastFmWrapper : SourceWrapper = SourceLastFmWrapper()
    private val nYTimesWrapper : SourceWrapper = SourceNYTimesWrapper()
    private val wikipediaWrapper : SourceWrapper = SourceWikipediaWrapper()

    override fun getSourceWrapper(source: Source) : SourceWrapper{
        return when(source) {
                    Source.LastFM -> lastFmWrapper
                    Source.NYTimes -> nYTimesWrapper
                    Source.Wikipedia -> wikipediaWrapper
                }
    }

}

interface SourceWrapper {
    fun getSource(): String
}

internal class SourceLastFmWrapper : SourceWrapper{
    override fun getSource(): String {
        return SOURCE_STRING + "LastFM"
    }
}

internal class SourceWikipediaWrapper : SourceWrapper{
    override fun getSource(): String {
        return SOURCE_STRING + "New York Times"
    }
}

internal class SourceNYTimesWrapper : SourceWrapper{
    override fun getSource(): String {
        return SOURCE_STRING + "Wikipedia"
    }
}

