package com.example.hansimcat

class Feed(
    val userId: String,
    val imageUrl: String,
    val profileImageUrl: String,
    var likeCount: Long,
    var isLike: Boolean,
    var isBookmark: Boolean
)