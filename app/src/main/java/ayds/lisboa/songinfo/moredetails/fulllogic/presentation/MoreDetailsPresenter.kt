package ayds.lisboa.songinfo.moredetails.fulllogic.presentation
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repositoryInterface.ArtistRepository
import ayds.observer.Observer

interface MoreDetailsPresenter {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository) : MoreDetailsPresenter {

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
            repository.getArtistData(moreDetailsView.uiState.artistName)
    }

    }