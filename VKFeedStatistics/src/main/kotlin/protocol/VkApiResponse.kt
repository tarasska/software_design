package protocol

import com.beust.klaxon.Json

data class VkApiResponse(val response: InnerResponse) {
    data class InnerResponse(@Json(name = "total_count") val postCount: Int)
}
