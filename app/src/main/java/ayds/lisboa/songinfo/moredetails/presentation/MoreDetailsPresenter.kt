package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.observer.Observable
import ayds.observer.Subject

private const val LOCALLY_SAVED = "[*]"
interface MoreDetailsPresenter {
    val artistObservable: Observable<Artist>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistRepository) :
    MoreDetailsPresenter {

    override val artistObservable = Subject<Artist>()
    private val artistInfoHelper : ArtistInfoHelper = MoreDetailsInjector.artistInfoHelper

    override fun getArtistMoreInformation(artistName: String) {
        Thread {
            notifyArtist(artistName)
        }.start()
    }

    private fun notifyArtist(artistName: String){
        val artistData: Artist = repository.getArtistData(artistName)
        artistData.applyFormattingInfoArtist(artistName)
        artistData.addLocallySavedMarkToInfo()
        artistObservable.notify(artistData)
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


