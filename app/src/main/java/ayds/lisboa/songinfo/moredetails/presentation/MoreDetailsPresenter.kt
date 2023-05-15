package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.observer.Observable
import ayds.observer.Subject

private const val LOCALLY_SAVED = "[*]"
interface MoreDetailsPresenter {
    val artistObservable: Observable<MoreDetailsUiState>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository,
    private val artistInfoHelper : ArtistInfoHelper) :
    MoreDetailsPresenter {

    override val artistObservable = Subject<MoreDetailsUiState>()
    private var uiState: MoreDetailsUiState = MoreDetailsUiState()

    override fun getArtistMoreInformation(artistName: String) {
        Thread {
            notifyArtist(artistName)
        }.start()
    }

    private fun notifyArtist(artistName: String){
        val artistData: Artist = repository.getArtistData(artistName)
        artistData.applyFormattingInfoArtist(artistName)
        artistData.addLocallySavedMarkToInfo()
        updateUIState(artistData)
        artistObservable.notify(uiState)
    }

    private fun updateUIState(artist: Artist) {
        when (artist) {
            is ArtistData -> updateArtistUIState(artist)
            Artist.EmptyArtist -> updateNoResultsUiState()
        }
    }
    private fun updateArtistUIState(artist: ArtistData) {

        uiState = uiState.copy(
            artistName = artist.artistName,
            infoArtist = artist.infoArtist,
            url = artist.url
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            infoArtist = "",
            url = ""
        )
    }

    private fun Artist.applyFormattingInfoArtist(artistName: String) {
        if( this is ArtistData){
            this.infoArtist = artistInfoHelper.textToHtml(this.infoArtist, artistName)
        }
    }

    private fun Artist.addLocallySavedMarkToInfo() {
     if (this is ArtistData) {
         if(isLocallyStored)
             infoArtist = "$LOCALLY_SAVED $infoArtist"
     }
 }
}


