package com.caetano.r.mypoint

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.caetano.r.mypoint.api.Api
import com.caetano.r.mypoint.api.Login
import com.caetano.r.mypoint.api.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val CLIENT_ID = "client_id"
    private val TOKEN = "token"
    private val EMAIL = "email"
    private val sharedPref by lazy { getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE) }

    companion object {
        private val LOADING = 0
        private val LOGIN = 1
        private val LOGGED = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val savedToken = sharedPref.getString(TOKEN, "")

        if (!TextUtils.isEmpty(savedToken)) {
            arrangeLayout(LOGGED)
        }

        btn_login.setOnClickListener {
            arrangeLayout(LOADING)
            doLogin()
        }
    }

    private fun doLogin() {
        val login = Login(edt_cpf.text.toString(), edt_password.text.toString())
        Api.service.signIn(login).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    arrangeLayout(LOGGED)
                    saveResponseValues(response.body())
                } else {
                    arrangeLayout(LOGIN)
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                arrangeLayout(LOGIN)
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveResponseValues(response: LoginResponse) {
        val editor = sharedPref.edit()
        editor.putString(CLIENT_ID, response.client_id)
        editor.putString(TOKEN, response.token)
        editor.putString(EMAIL, response.data.email)
        editor.apply()
    }

    private fun arrangeLayout(mode: Int) {
        when (mode) {
            LOADING -> {
                layout_login.visibility = View.GONE
                layout_loading.visibility = View.VISIBLE
                layout_logged.visibility = View.GONE
            }
            LOGIN -> {
                layout_login.visibility = View.VISIBLE
                layout_loading.visibility = View.GONE
                layout_logged.visibility = View.GONE
            }
            LOGGED -> {
                layout_login.visibility = View.GONE
                layout_loading.visibility = View.GONE
                layout_logged.visibility = View.VISIBLE
            }
        }
    }
}