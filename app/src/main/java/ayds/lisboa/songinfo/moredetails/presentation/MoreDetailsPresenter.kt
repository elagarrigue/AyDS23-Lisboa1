package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repositoryInterface.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsPresenter {
    val artistObservable: Observable<Artist>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistRepository) :
    MoreDetailsPresenter {

    override val artistObservable = Subject<Artist>()

    override fun getArtistMoreInformation(artistName: String) {
        val artistData: Artist = repository.getArtistData(artistName)
        artistObservable.notify(artistData)
    }

}


