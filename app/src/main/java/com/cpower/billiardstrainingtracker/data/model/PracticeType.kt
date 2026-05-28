package com.cpower.billiardstrainingtracker.data.model

enum class PracticeType(val label: String) {
    DRAW("拉桿"),
    PUSH("推桿"),
    STOP("定桿"),
    HIGH_LOW("高低桿"),
    LONG("遠球"),
    THIN("薄球"),
    BREAK("開球"),
    ;

    val displayName: String
        get() = when (this) {
            HIGH_LOW -> "高低桿控制"
            else -> "${label}練習"
        }

    companion object {
        fun fromStoredValue(value: String): PracticeType =
            entries.firstOrNull { it.name == value } ?: DRAW
    }
}
