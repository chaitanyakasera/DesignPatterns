package com.cnm.mvc.login

import com.cnm.mvc.login.interfaces.IController

class Controller(private val model: IController, private val onDatasetChange: IDataSetChange) {
    private var _userName = ""
    private var _password = ""

    fun setUserName(charsets: String = "") {
        _userName = charsets
    }

    fun setPassword(charsets: String = "") {
        _password = charsets
    }

    fun clearAll() {
        _userName = ""
        _password = ""
    }

    fun onSubmitPressed() {
        if (model.fetchUser(_userName, _password)) {
            onDatasetChange.onDataUpdated()
        } else {
            onDatasetChange.onError("Invalid Credentials!")
        }
    }

    interface IDataSetChange {
        fun onDataUpdated()
        fun onError(message: String)
    }
}