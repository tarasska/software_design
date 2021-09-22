package protocol

class VkQueryBuilder(private val config: VkApiConfig) {
    fun buildHashTagCntQuery(hashTag: String, startTimeSec: Long, endTimeSec: Long): String {
        return "https://api.vk.com:${config.publicConfig.port}/method/newsfeed.search?" +
                "q=%23$hashTag" +
                "&v=${config.publicConfig.version.major}.${config.publicConfig.version.minor}" +
                "&access_token=${config.privateConfig.access_token}" +
                "&count=0" +
                "&start_time=$startTimeSec" +
                "&end_time=$endTimeSec"
    }
}