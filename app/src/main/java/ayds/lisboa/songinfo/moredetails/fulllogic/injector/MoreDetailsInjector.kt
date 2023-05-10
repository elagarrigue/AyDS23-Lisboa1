package ayds.lisboa.songinfo.moredetails.fulllogic.injector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.fulllogic.OtherInfoWindow
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repositoryInterface.ArtistRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.external.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.external.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.local.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.MoreDetailsDomain
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.MoreDetailsDomainImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenterImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.MoreDetailsView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsInjector {

    private lateinit var moreDetailsPresenter : MoreDetailsPresenter
    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsModel(view: MoreDetailsView) {
        val artistLocalStorage : ArtistLocalStorage = ArtistLocalStorageImpl(view as Context)

        val lastFMAPI = createLastFMAPI()
        val lastFMService : LastFMService = LastFMServiceImpl(lastFMAPI, JSONToArtistDataResolver())

        val repository : ArtistRepository = ArtistRepositoryImpl(artistLocalStorage, lastFMService)

        moreDetailsPresenter = MoreDetailsPresenterImpl(repository)
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(OtherInfoWindow.ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

}