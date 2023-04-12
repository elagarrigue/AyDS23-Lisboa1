package ayds.lisboa.songinfo.home.view

object FormatterFactory {
    private val dayWrapper : DateFormatWrapper = DateFormatWrapperDay()
    private val monthWrapper : DateFormatWrapper = DateFormatWrapperMonth()
    //private val yearWrapper : DateFormatWrapper = DateFormatWrapperYear()
    fun getWrapper(precision: String): DateFormatWrapper {
        return when (precision) {
            "day" -> dayWrapper
            "month" -> monthWrapper
            //"year" -> yearWrapper
            else -> error("Invalid precision")
        }
    }
}
interface DateFormatWrapper {
    fun getReleaseDateFormat(releaseDate: String, precision: String): String

}

internal class DateFormatWrapperDay : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String, precision: String): String {
        val day = releaseDate.split("-")[2]
        val month = releaseDate.split("-")[1]
        val year = releaseDate.split("-")[0]

        return day+"/"+month+ "/" + year
    }
}

internal class DateFormatWrapperMonth : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String, precision: String): String {
        val month = releaseDate.split("-")[1]
        val year = releaseDate.split("-")[0]

        return getMonthName(month) + ", " + year
    }

    private fun getMonthName(month : String): String? {
       return when(month) {
           "01" -> "January"
           "02" -> "February"
           "03" -> "March"
           "04" -> "April"
           "05" -> "May"
           "06" -> "June"
           "07" -> "July"
           "08" -> "August"
           "09" -> "September"
           "10" -> "October"
           "11" -> "November"
           "12" -> "December"
           else -> error("Invalid month number")
       }
    }
}

internal class DateFormatWrapperImpl : DateFormatWrapper {
        override fun getReleaseDateFormat(releaseDate: String, precision: String): String {
            val splitReleaseDate = releaseDate.split("-")
            return when (precision) {
                "year" -> splitReleaseDate.first() +
                        if (isLeapYear(Integer.parseInt(releaseDate))) {
                            " (Leap year)"
                        } else {
                            " (Not a leap year)"
                        }

                else -> splitReleaseDate[2] + "/" + splitReleaseDate[1] + "/" + splitReleaseDate[0]
            }
        }

        private fun isLeapYear(year: Int): Boolean {
            return (((year % 4) == 0) && (((year % 100) != 0) || ((year % 400) == 0)))
        }

}