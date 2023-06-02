package ayds.lisboa.songinfo.moredetails.injector

import android.content.Context
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.lisboa.songinfo.moredetails.data.repository.external.CardBroker
import ayds.lisboa.songinfo.moredetails.data.repository.external.CardBrokerImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.data.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.external.proxys.LastFMProxy
import ayds.lisboa.songinfo.moredetails.data.repository.external.proxys.NYTimesProxy
import ayds.lisboa.songinfo.moredetails.data.repository.external.proxys.ServiceProxy
import ayds.lisboa.songinfo.moredetails.data.repository.external.proxys.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.data.repository.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.data.repository.local.CardLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.repository.local.CursorToCardDataMapperImpl
import ayds.lisboa.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.lisboa1.lastfm.LastFMInjector
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(context: Context) {
        val cardLocalStorage : CardLocalStorage =
            CardLocalStorageImpl(context, CursorToCardDataMapperImpl())

        val lastFMProxy : ServiceProxy = LastFMProxy(LastFMInjector.getLastFMService())
        val nyTimesProxy : ServiceProxy = NYTimesProxy(NYTimesInjector.nyTimesService)
        val wikipediaProxy: ServiceProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)

        val proxyList: MutableList<ServiceProxy> = ArrayList()
        proxyList.add(lastFMProxy)
        proxyList.add(nyTimesProxy)
        proxyList.add(wikipediaProxy)

        val broker : CardBroker = CardBrokerImpl(proxyList)

        val repository : CardRepository = CardRepositoryImpl(cardLocalStorage,broker)

        moreDetailsPresenter = MoreDetailsPresenterImpl(repository, CardDescriptionHelperImpl())
    }

}