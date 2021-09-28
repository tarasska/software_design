package app

import client.VkHttpClient
import protocol.HashTagCntResponse
import java.time.Duration
import java.time.Instant

class HashTagCounter(private val client: VkHttpClient) {

    suspend fun count(hastTag: String, hours: Int): List<Int> {
        val cntPerHour = ArrayList<Int>()
        val now = Instant.now()
        for (i in 1..(hours.toLong())) {
            val resp = client.countPostByHashtag(
                hastTag,
                (now - Duration.ofHours(i)).epochSecond,
                (now - Duration.ofHours(i - 1)).epochSecond
            )
            cntPerHour.add(when (resp) {
                is HashTagCntResponse -> resp.response.postCount
                else -> -1
            })
        }
        return cntPerHour
    }
}