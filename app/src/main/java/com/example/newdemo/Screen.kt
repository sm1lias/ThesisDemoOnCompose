package com.example.newdemo

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ResultScreen : Screen("result_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }

}
