package com.june.chattutorial.model

data class ChatItemModel(
    val senderId: String?,
    val message: String?,
    val sendTime: Long?
) {
    constructor(): this("","", 0)
}
