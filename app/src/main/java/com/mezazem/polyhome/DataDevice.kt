package com.mezazem.polyhome

data class DataDevice(
    val id: String,
    val type: String,
    val availableCommands: List<String>,
    var opening: Int? = null,
    val power: Int? = null
)