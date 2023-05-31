package ayds.lisboa.songinfo.moredetails.injector

import android.content.Context
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistBroker
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistBrokerImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.proxys.LastFMProxy
import ayds.lisboa.songinfo.moredetails.data.repository.proxys.NYTimesProxy
import ayds.lisboa.songinfo.moredetails.data.repository.ServiceProxy
import ayds.lisboa.songinfo.moredetails.data.repository.proxys.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.repository.local.CursorToArtistDataMapperImpl
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.lisboa1.lastfm.LastFMInjector
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(context: Context) {
        val artistLocalStorage : ArtistLocalStorage =
            ArtistLocalStorageImpl(context, CursorToArtistDataMapperImpl())

        val lastFMProxy : ServiceProxy = LastFMProxy(LastFMInjector.getLastFMService())
        val nyTimesProxy : ServiceProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
        val wikipediaProxy: ServiceProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)

        val proxyList: MutableList<ServiceProxy> = ArrayList()
        proxyList.add(lastFMProxy)
        proxyList.add(nyTimesProxy)
        proxyList.add(wikipediaProxy)

        val broker : ArtistBroker = ArtistBrokerImpl(proxyList)

        val repository : ArtistRepository = ArtistRepositoryImpl(artistLocalStorage,broker)

        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, ArtistInfoHelperImpl())
    }

}