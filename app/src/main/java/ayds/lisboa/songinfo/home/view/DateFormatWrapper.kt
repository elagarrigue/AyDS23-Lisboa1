package ayds.lisboa.songinfo.home.view

interface FormatterFactory {
    fun getWrapper(precision: String): DateFormatWrapper
}

private const val DAY = "day"
private const val MONTH = "month"
private const val YEAR = "year"

internal class FormatterFactoryImpl : FormatterFactory {
    private val dayWrapper : DateFormatWrapper = DateFormatWrapperDay()
    private val monthWrapper : DateFormatWrapper = DateFormatWrapperMonth()
    private val yearWrapper : DateFormatWrapper = DateFormatWrapperYear()
    private val defaultWrapper : DateFormatWrapper = DateFormatWrapperDefault()

    override fun getWrapper(precision: String): DateFormatWrapper {
        return when (precision) {
            DAY -> dayWrapper
            MONTH -> monthWrapper
            YEAR -> yearWrapper
            else -> defaultWrapper
        }
    }
}
interface DateFormatWrapper {
    fun getReleaseDateFormat(releaseDate: String): String
}

internal class DateFormatWrapperDay : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String): String {
        val day = releaseDate.split("-")[2]
        val month = releaseDate.split("-")[1]
        val year = releaseDate.split("-")[0]
        return "$day/$month/$year"
    }
}

internal class DateFormatWrapperMonth : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String): String {
        val month = releaseDate.split("-")[1]
        val year = releaseDate.split("-")[0]
        return getMonthName(month) + ", " + year
    }

    private fun getMonthName(month : String): String {
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

internal class DateFormatWrapperYear : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String): String {
        val year = releaseDate.split("-")[0]
        return year + if (isLeapYear(Integer.parseInt(year)))
            " (Leap year)" else
            " (Not a leap year)"
    }

    private fun isLeapYear(year: Int): Boolean {
        return (((year % 4) == 0) && (((year % 100) != 0) || ((year % 400) == 0)))
    }

}

internal class DateFormatWrapperDefault : DateFormatWrapper {
    override fun getReleaseDateFormat(releaseDate: String): String {
        return releaseDate
    }

}