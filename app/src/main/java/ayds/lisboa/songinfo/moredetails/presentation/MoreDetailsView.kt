package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.OtherInfoWindow
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.ImageLoader

interface MoreDetailsView {
    var uiState: MoreDetailsUiState
}

class MoreDetailsActivity : AppCompatActivity(), MoreDetailsView {

    private lateinit var artistInfoPanel: TextView
    private lateinit var openURLListener: View
    private lateinit var imageLastFMAPI: ImageView
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader


    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initModule()
        initObservers()
        setContentView(R.layout.activity_other_info)
        initProperties()
        initArtistName()
    }

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            imageLoader.loadImageIntoView(OtherInfoWindow.imageUrl, imageLastFMAPI)
            artistInfoPanel.text = infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

    private fun initArtistName(){
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName?.let { moreDetailsPresenter.getArtistMoreInformation(it) }
    }

    private fun initModule() {
        MoreDetailsInjector.initMoreDetailsModel(this)
        moreDetailsPresenter = MoreDetailsInjector.getPresenter()
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.textPane2)
        openURLListener = findViewById(R.id.openUrlButton)
        imageLastFMAPI = findViewById(R.id.imageView)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistObservable.subscribe { value -> updateUIState(value) }
    }

    private fun updateUIState(artistData: Artist) {
        when (artistData) {
            is Artist.ArtistData -> updateArtistUIState(artistData)
            Artist.EmptyArtist -> updateNoResultsUiState()
        }
    }

    private fun updateArtistUIState(artistData: Artist.ArtistData) {
        uiState = uiState.copy(
            artistName = artistData.artistName,
            infoArtist = artistData.infoArtist,
            url = artistData.url,
            isLocallyStored = artistData.isLocallyStored,
        )
        showArtistInfo(uiState.infoArtist)
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            infoArtist = "",
            url = "",
            isLocallyStored = false,
        )
    }

}