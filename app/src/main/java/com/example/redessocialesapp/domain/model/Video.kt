package com.example.redessocialesapp.domain.model

import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit

data class Video(
    var videoId: String = "",
    var title: String = "",
    var description: String = "",
    var category: String = "",
    var imageURL: String = "",
    var videoURL: String = "",
    var location: String = "",
    var date: Timestamp = Timestamp.now(),
    var likes: Int = 0,
    var views: Int = 0
) {
    fun getTimeAgo(timestamp: Timestamp?): String {
        if (timestamp == null) return "Desconocido"

        val timeNow = System.currentTimeMillis()
        val timeArticle = timestamp.toDate().time

        val diff = timeNow - timeArticle

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            seconds < 60 -> "hace ${seconds} segundos"
            minutes < 60 -> "hace ${minutes} minutos"
            hours < 24 -> "hace ${hours} horas"
            days < 7 -> "hace ${days} días"
            days < 30 -> "hace ${days / 7} semanas"
            days < 365 -> "hace ${days / 30} meses"
            else -> "hace ${days / 365} años"
        }
    }
}