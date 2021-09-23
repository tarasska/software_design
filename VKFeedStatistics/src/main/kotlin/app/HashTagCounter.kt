package app

import client.VkHttpClient
import java.time.Duration
import java.time.Instant

class HashTagCounter(private val client: VkHttpClient) {

    suspend fun count(hastTag: String, hours: Int): List<Int> {
        val cntPerHour = ArrayList<Int>()
        val now = Instant.now()
        for (i in 1..(hours.toLong())) {
            cntPerHour.add(client.countPostByHashtag(
                hastTag,
                (now - Duration.ofHours(i)).epochSecond,
                now.epochSecond
            )?.response?.postCount ?: -1)
        }
        return cntPerHour
    }
}