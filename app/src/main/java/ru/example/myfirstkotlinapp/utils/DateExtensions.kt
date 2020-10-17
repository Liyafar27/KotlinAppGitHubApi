package ru.example.myfirstkotlinapp.utils

import java.util.*

fun Date.removeTime(): Date {
    val cal = Calendar.getInstance() // get calendar instance
    cal.time=this
//    val  sdf =  SimpleDateFormat("dd/MM/yyyy")
    cal[Calendar.DAY_OF_MONTH] = 1
    cal[Calendar.HOUR_OF_DAY] = 0 // set hour to midnight
    cal[Calendar.MINUTE] = 0 // set minute in hour
    cal[Calendar.SECOND] = 0 // set second in minute
    cal[Calendar.MILLISECOND] = 0 // set millisecond in second

    return cal.time
//    sdf.format(

}



