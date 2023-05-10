package ayds.lisboa.songinfo.moredetails.fulllogic.domain
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistData
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repositoryInterface.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsDomain {
    val moreInformationObservable: Observable<ArtistData>
    fun getArtistMoreInformation(term: String)
}

internal class MoreDetailsDomainImpl(private val repository: ArtistRepository): MoreDetailsDomain {
    override val moreInformationObservable = Subject<ArtistData>()
    override fun getArtistMoreInformation(artistName: String) {
        repository.getArtistData(artistName).let {
            moreInformationObservable.notify(it)
        }
    }
}