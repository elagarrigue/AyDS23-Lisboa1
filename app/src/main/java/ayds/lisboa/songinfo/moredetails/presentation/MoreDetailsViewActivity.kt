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

    private lateinit var cardInfoPanel: TextView
    private lateinit var sourceText: TextView
    private lateinit var fullArticleButton: View
    private lateinit var previousCard: View
    private lateinit var nextCard: View
    private lateinit var imageLogo: ImageView
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var uiState: MoreDetailsUiState
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader


    companion object {
        const val CARD_NAME_EXTRA = "cardName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initModule()
        initObservers()
        setContentView(R.layout.activity_other_info)
        initProperties()
        initCardName()
    }

    private fun initModule() {
        MoreDetailsInjector.initMoreDetailsPresenter(this)
        moreDetailsPresenter = MoreDetailsInjector.getPresenter()
    }

    private fun initObservers() {
        moreDetailsPresenter.uiStateObservable.subscribe { value -> updateUiWithCardInfo(value) }
    }

    private fun initProperties() {
        cardInfoPanel = findViewById(R.id.cardInfoPanel)
        sourceText = findViewById(R.id.sourceText)
        fullArticleButton = findViewById(R.id.fullArticleButton)
        previousCard = findViewById(R.id.previousCard)
        nextCard = findViewById(R.id.nextCard)
        imageLogo = findViewById(R.id.imageLogo)
    }

    private fun initCardName() {
        val cardName = intent.getStringExtra(CARD_NAME_EXTRA)
        cardName?.let { moreDetailsPresenter.getCardMoreInformation(it) }
    }

    private fun updateUiWithCardInfo(uiState: MoreDetailsUiState) {
        this.uiState = uiState
        val cardDataStates = uiState.cardsDataState
        setFullArticleButtonListener(cardDataStates[0].infoURL)
        setNavegationButtonsListeners()
        showCardInfo(cardDataStates[0])
    }

    private fun setFullArticleButtonListener(cardURL: String) {
        if (cardURL != "") {
            fullArticleButton.setOnClickListener {
                openURL(cardURL)
            }
        }
    }

    private fun setNavegationButtonsListeners() {
        previousCard.setOnClickListener {
            showPreviousCardInfo()
        }
        nextCard.setOnClickListener {
            showNextCardInfo()
        }
    }

    private fun showCardInfo(cardUiState: CardDataState) {
        runOnUiThread {
            if (cardUiState.sourceLogo != "") imageLoader.loadImageIntoView(
                cardUiState.sourceLogo,
                imageLogo
            )
            cardInfoPanel.text = cardUiState.description.let { HtmlCompat.fromHtml(it, 0) }
            sourceText.text = cardUiState.sourceName.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showPreviousCardInfo() {
        val cardList = uiState.cardsDataState
        val currentIndex = uiState.selectedIndex
        val previousIndex = if (currentIndex == 0) cardList.size - 1 else currentIndex - 1

        val previousCardUiState = cardList[previousIndex]
        setFullArticleButtonListener(previousCardUiState.infoURL)
        showCardInfo(previousCardUiState)

        uiState.selectedIndex = previousIndex
    }

    private fun showNextCardInfo() {
        val cardList = uiState.cardsDataState
        val currentIndex = uiState.selectedIndex
        val nextIndex = (currentIndex + 1) % cardList.size

        val nextCardUiState = cardList[nextIndex]
        setFullArticleButtonListener(nextCardUiState.infoURL)
        showCardInfo(nextCardUiState)

        uiState.selectedIndex = nextIndex
    }

}