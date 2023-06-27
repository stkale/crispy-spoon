package com.github.stkale.spoon.domain

import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    val id: Int,
    val name: String
)
