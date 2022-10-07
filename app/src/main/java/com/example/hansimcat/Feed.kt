package com.example.hansimcat

data class Feed(
    val userId: String,
    val imageUrl: String,
    val profileImageUrl: String,
    var likeCount: Long,
    var isLike: Boolean,
    var isBookmark: Boolean,
    var content: String,
    val uid2: String
)