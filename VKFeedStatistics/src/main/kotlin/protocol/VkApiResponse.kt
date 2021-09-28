package protocol

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

sealed class VkApiResponse {
    companion object {
        fun of(json: String): VkApiResponse {
            return runCatching {
                Klaxon().parse<HashTagCntResponse>(json)!!
            }.recoverCatching{
                Klaxon().parse<ErrorResponse>(json)!!
            }.getOrDefault(UndefinedResponse(json))
        }
    }
}

data class HashTagCntResponse(val response: InnerResponse): VkApiResponse() {
    data class InnerResponse(@Json(name = "total_count") val postCount: Int)
}

data class ErrorResponse(val error: ErrorInfo): VkApiResponse() {
    data class ErrorInfo(
        @Json(name = "error_code") val errorCode: Int,
        @Json(name = "error_msg") val errorMsg: String
    )
}

data class UndefinedResponse(var rawResult: String): VkApiResponse()
