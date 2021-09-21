package protocol

import com.beust.klaxon.Json

data class VkApiResponse(@Json(name = "total_count") val postCount: Int)
