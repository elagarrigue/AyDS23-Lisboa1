package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.observer.Observable

interface MoreDetailsView{
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState
}

class MoreDetailsActivity : AppCompatActivity(), MoreDetailsView {

    private lateinit var artistInfoPanel: TextView
    private lateinit var openURLListener: View
    private lateinit var imageLastFMAPI: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other_info)
        initProperties()
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.textPane2)
        openURLListener = findViewById(R.id.openUrlButton)
        imageLastFMAPI = findViewById(R.id.imageView)
    }

}