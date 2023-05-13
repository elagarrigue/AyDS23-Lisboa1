package ayds.lisboa.songinfo.moredetails.injector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.domain.repositoryInterface.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.repository.external.JSONToArtistDataResolver
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMAPI
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMService
import ayds.lisboa.songinfo.moredetails.data.repository.local.CursorToArtistDataMapperImpl
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val ARTIST_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {

    private lateinit var moreDetailsPresenter : MoreDetailsPresenter
    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(context: Context) {
        val artistLocalStorage : ArtistLocalStorage =
            ArtistLocalStorageImpl(context, CursorToArtistDataMapperImpl())

        val lastFMAPI = createLastFMAPI()
        val lastFMService : LastFMService = LastFMServiceImpl(lastFMAPI, JSONToArtistDataResolver())

        val repository : ArtistRepository = ArtistRepositoryImpl(artistLocalStorage, lastFMService)

        moreDetailsPresenter = MoreDetailsPresenterImpl(repository)
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

}