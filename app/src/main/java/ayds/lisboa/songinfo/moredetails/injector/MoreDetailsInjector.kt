package ayds.lisboa.songinfo.moredetails.injector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorageImpl
import ayds.lisboa1.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.data.repository.local.CursorToArtistDataMapperImpl
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.lisboa1.lastfm.LastFMInjector

object MoreDetailsInjector {

    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(context: Context) {
        val artistLocalStorage : ArtistLocalStorage =
            ArtistLocalStorageImpl(context, CursorToArtistDataMapperImpl())

        val lastFMService : LastFMService = LastFMInjector.getLastFMService()

        val repository : ArtistRepository = ArtistRepositoryImpl(artistLocalStorage, lastFMService)

        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, ArtistInfoHelperImpl())
    }

}