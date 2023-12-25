package com.cnm.mvc.login.interfaces

interface IController {
    fun fetchUser(userName: String, password: String): Boolean
}

interface IView {
    fun getSelectedUserJson(): String
}