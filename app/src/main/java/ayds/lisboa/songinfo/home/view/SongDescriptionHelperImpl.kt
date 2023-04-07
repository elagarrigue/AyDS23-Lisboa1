package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release Date: ${formatearReleaseDate(song.releaseDate,song.releaseDatePrecision)}"
            else -> "Song not found"
        }
    }

    private fun formatearReleaseDate(releaseDate: String, precision: String)=
        when(precision){
            "year" -> releaseDate.split("-").first()
            "month" -> releaseDate.split("-").take(2).joinToString("-")
            else -> releaseDate
        }

}