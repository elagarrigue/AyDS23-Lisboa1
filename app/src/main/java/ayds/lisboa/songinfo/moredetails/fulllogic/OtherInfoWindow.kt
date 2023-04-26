package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        open(intent.getStringExtra("artistName"))
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            val infoArtist = getInfoArtistFromDatabase(artistName) ?: artistName.getInfoArtistFromAPI()
            showArtistInfo(infoArtist)
        }.start()
    }

    private fun open(artist: String?) {
        initProperties()
        getArtistInfo(artist)
    }

    private fun getInfoArtistFromDatabase(artistName: String?): String? {
        val infoArtist = DataBase.getInfo(dataBase, artistName)
        return if (infoArtist != null) "[*]$infoArtist" else null
    }

    private fun String?.getInfoArtistFromAPI(): String?{
        var infoArtist = "No Results"
        val jObjectArtist = this.getJObjectArtist()
        try {
            if (jObjectArtist.getArtistBioContent() != null) {
                infoArtist = textToHtml(
                    jObjectArtist.getArtistBioContent().
                    asString.replace("\\n", "\n"), this
                )
                DataBase.saveArtist(dataBase, this, infoArtist)
            }
            jObjectArtist.getArtistURL().asString.setOpenUrlButtonClickListener()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return infoArtist
    }

    private fun JsonObject.getArtistBioContent() = this["artist"].asJsonObject["bio"].asJsonObject["content"]
    private fun JsonObject.getArtistURL() = this["artist"].asJsonObject["URL"]

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun String?.getJObjectArtist(): JsonObject {
        return Gson().fromJson(
            createLastFMAPI().getArtistInfo(this).execute().body(), JsonObject::class.java
        )
    }

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2?.text = Html.fromHtml(infoArtist)
        }
    }

    private fun String.setOpenUrlButtonClickListener() {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(this)
            startActivity(intent)
        }
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        dataBase = DataBase(this)
        textPane2 = findViewById(R.id.textPane2)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text.replace("'", " ").replace("\n", "<br>").replace(
            "(?i)$term".toRegex(), "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
        )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    companion object {
        const val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}