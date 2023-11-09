package com.chrrissoft.services.navigation

enum class Screen(val route: String, val label: String) {
    BACKGROUND(route = "background", label = "Background"),
    FOREGROUND(route = "foreground", label = "Foreground"),
    BOUND(route = "bound", label = "Bound");

    companion object {
        val screens = buildList {
            add(BACKGROUND)
            add(FOREGROUND)
            add(BOUND)
        }
    }
}
