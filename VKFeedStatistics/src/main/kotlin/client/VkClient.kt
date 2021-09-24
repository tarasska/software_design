package client

import protocol.VkApiResponse
import java.io.Closeable

interface VkClient : Closeable {
    /**
     *  Counts the number of posts by a hashtag in a given period of time.
     *
     *  @param hashTag - hashtag to search
     *  @param hoursLimit - the number of last hours for which posts are counted
     */
    suspend fun countPostByHashtag(hashTag: String, hoursLimit: Long): VkApiResponse

    /**
     *  Counts the number of posts by a hashtag in a given period of time.
     *
     *  @param hashTag - hashtag to search
     *  @param startTimeSec - start unix-time in seconds
     *  @param endTimeSec - end unix-time in seconds
     */
    suspend fun countPostByHashtag(hashTag: String, startTimeSec: Long, endTimeSec: Long): VkApiResponse
}