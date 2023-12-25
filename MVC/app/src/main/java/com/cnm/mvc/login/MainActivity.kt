package com.cnm.mvc.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.cnm.mvc.R
import com.cnm.mvc.databinding.ActivityMainBinding
import com.cnm.mvc.hide
import com.cnm.mvc.login.interfaces.IView
import com.cnm.mvc.show
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var bi: ActivityMainBinding
    private val model by lazy {
        val ins = this.resources.openRawResource(
            resources.getIdentifier(
                "user_data", "raw", packageName
            )
        )
        val jsonString = StringBuilder()
        val reader = BufferedReader(InputStreamReader(ins, StandardCharsets.UTF_8))
        var line: String? = ""
        while (true) {
            line = reader.readLine()
            if (line != null) {
                jsonString.append(line)
            } else {
                break
            }
        }
        reader.close()
        val type = object : TypeToken<List<User>>() {}.type
        val list = Gson().fromJson<List<User>>(jsonString.toString(), type)
        Model(list)
    }
    private val dataChangeListener by lazy {
        object : Controller.IDataSetChange {
            override fun onDataUpdated() {
              setUpData()
            }

            override fun onError(message: String) {
                bi.tvPasswordError.text = message
                bi.tvPasswordError.show()
            }
        }
    }
    private val controller by lazy {
        Controller(model, dataChangeListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpView()
    }

    private fun setUpView() {
        bi.etUsername.doAfterTextChanged {
            controller.setUserName(it.toString())
            removeAllErrors()
            cleanSuccessView()
        }
        bi.etPassword.doAfterTextChanged {
            controller.setPassword(it.toString())
            removeAllErrors()
            cleanSuccessView()
        }
        bi.buttonLogin.setOnClickListener {
            controller.onSubmitPressed()
        }
        bi.ivPassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun cleanViews() {
        bi.etUsername.setText("")
        bi.etPassword.setText("")
    }

    private fun cleanSuccessView() {
        bi.tvSuccess.text = ""
    }

    private fun removeAllErrors() {
        bi.tvPasswordError.hide()
        bi.tvUserNameError.hide()
    }

    private fun togglePasswordVisibility() {
        val position = bi.etPassword.selectionEnd
        if (bi.etPassword.transformationMethod is PasswordTransformationMethod) {
            bi.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            bi.ivPassword.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.ic_eye_24))
        } else {
            bi.etPassword.transformationMethod = PasswordTransformationMethod()
            bi.ivPassword.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.ic_eye_24))
        }
        bi.etPassword.setSelection(position)
    }


    private fun setUpData() {
        if ((model as IView).getSelectedUserJson().isNotBlank()) {
            cleanViews()
            controller.clearAll()
            bi.tvSuccess.text = buildString {
                append("Logged in Successfully")
                append("\n")
                append(model.getSelectedUserJson())
            }
        } else {
            bi.tvSuccess.text = ""
            removeAllErrors()
        }

    }
}