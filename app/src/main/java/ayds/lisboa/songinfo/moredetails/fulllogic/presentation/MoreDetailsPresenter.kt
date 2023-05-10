package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.MoreDetailsDomain
import ayds.observer.Observer

interface MoreDetailsPresenter {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsPresenterImpl(
    private val moreDetailsDomain: MoreDetailsDomain) : MoreDetailsPresenter {

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsUiEvent> =
        Observer { value ->
            when (value) {
                MoreDetailsUiEvent.ViewFullArticle -> getArtistMoreInformation()
            }
        }

    private fun getArtistMoreInformation() {
            moreDetailsDomain.getArtistMoreInformation(moreDetailsView.uiState.artistName)
    }

    }