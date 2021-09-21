package protocol

class VkQueryBuilder(private val config: VkApiConfig) {
    fun buildHashTagCntQuery(hashTag: String, startTimeSec: Long, endTimeSec: Long): String {
        //TODO: add fields to config
        return "https://api.vk.com/method/newsfeed.search?" +
                "q=#$hashTag" +
                "v=REPLACE_WITH_CONFIG" +
                "access_token=REPLACE_WITH_CONFIG" +
                "count=0" +
                "start_time=$startTimeSec" +
                "end_time=$endTimeSec"
    }
}