package com.github.stkale.spoon.domain

import kotlinx.serialization.Serializable

@Serializable
data class Charon(
    val id: Int,
    val name: String
)
