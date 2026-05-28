package com.cpower.billiardstrainingtracker.data.model

enum class Difficulty(val label: String) {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard"),
    ;

    companion object {
        fun fromStoredValue(value: String): Difficulty =
            entries.firstOrNull { it.name == value } ?: NORMAL
    }
}
