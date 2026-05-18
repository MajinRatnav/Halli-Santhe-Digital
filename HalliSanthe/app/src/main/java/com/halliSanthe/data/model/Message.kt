package com.halliSanthe.data.model

data class Message(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val buyerName: String = "",
    val buyerPhone: String = "",
    val message: String = "",
    val artisanId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.PENDING
)

enum class MessageStatus {
    PENDING, READ, REPLIED
}
