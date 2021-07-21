package com.ak.elinexttestproject.view.list

data class ImageListItem(
    val id: Int,
    val imageUrl: String = "",
    val isLoading: Boolean = true
)