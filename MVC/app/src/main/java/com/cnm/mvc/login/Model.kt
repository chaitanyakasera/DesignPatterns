package com.cnm.mvc.login

import com.cnm.mvc.login.interfaces.IController
import com.cnm.mvc.login.interfaces.IView
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


class Model(private val userList: List<User>) : IController, IView {
    private var selectedUser: User? = null

    override fun getSelectedUserJson(): String {
        return if (selectedUser == null) {
            ""
        } else {
            Gson().toJson(selectedUser)
        }
    }

    override fun fetchUser(userName: String, password: String): Boolean {
        val matchFound = userList.stream().anyMatch{
            if(it.email.equals(userName) && it.password.equals(password)){
                selectedUser = it
            }
            it.email.equals(userName) && it.password.equals(password)
        }
        return matchFound
    }


}

data class User(
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("username")
    val username: String
)