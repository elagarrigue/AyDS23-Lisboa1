package ayds.lisboa.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.ImageLoader

class MoreDetailsViewActivity : AppCompatActivity() {

    private lateinit var artistInfoPanel: TextView
    private lateinit var sourceText: TextView
    private lateinit var fullArticleButton: View
    private lateinit var previousCard: View
    private lateinit var nextCard: View
    private lateinit var imageLogo: ImageView
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var cardDataStates: List<CardDataState>
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader


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

    private fun initArtistName() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName?.let { moreDetailsPresenter.getArtistMoreInformation(it) }
    }

    private fun initModule() {
        MoreDetailsInjector.initMoreDetailsPresenter(this)
        moreDetailsPresenter = MoreDetailsInjector.getPresenter()
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.artistInfoPanel)
        sourceText = findViewById(R.id.sourceText)
        fullArticleButton = findViewById(R.id.fullArticleButton)
        imageLogo = findViewById(R.id.imageLogo)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistObservable.subscribe { value -> updateArtistInfo(value.artistCards) }
    }

    private fun updateArtistInfo(cardDataStates: List<CardDataState>) {
        this.cardDataStates = cardDataStates
        cardDataStates[0].setCardStateSelected()
        setFullArticleButtonListener(cardDataStates[0].infoURL)
        setNavegationButtonsListeners()
        showArtistInfo(cardDataStates[0])
    }

    private fun showArtistInfo(cardUiState: CardDataState) {
        runOnUiThread {
            imageLoader.loadImageIntoView(cardUiState.sourceLogo, imageLogo)
            artistInfoPanel.text = cardUiState.description.let { HtmlCompat.fromHtml(it, 0) }
            sourceText.text = cardUiState.source.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

    private fun showPreviousArtistInfo() {
        val currentIndex = cardDataStates.indexOfFirst { it.isSelected }
        val previousIndex = if (currentIndex == 0) cardDataStates.size - 1 else currentIndex - 1

        val previousCardUiState = cardDataStates[previousIndex]
        setFullArticleButtonListener(previousCardUiState.infoURL)
        showArtistInfo(previousCardUiState)

        cardDataStates[currentIndex].setCardStateUnselected()
        cardDataStates[previousIndex].setCardStateSelected()
    }

    private fun showNextArtistInfo() {
        val currentIndex = cardDataStates.indexOfFirst { it.isSelected }
        val nextIndex = (currentIndex + 1) % cardDataStates.size

        val nextCardUiState = cardDataStates[nextIndex]
        setFullArticleButtonListener(nextCardUiState.infoURL)
        showArtistInfo(nextCardUiState)

        cardDataStates[currentIndex].setCardStateUnselected()
        cardDataStates[nextIndex].setCardStateSelected()
    }

    private fun CardDataState.setCardStateSelected() {
        isSelected = true
    }

    private fun CardDataState.setCardStateUnselected() {
        isSelected = false
    }

    private fun setFullArticleButtonListener(artistURL: String) {
        fullArticleButton.setOnClickListener {
            openURL(artistURL)
        }
    }
    private fun setNavegationButtonsListeners() {
        previousCard.setOnClickListener {
            showPreviousArtistInfo()
        }
        nextCard.setOnClickListener {
            showNextArtistInfo()
        }
    }

    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}