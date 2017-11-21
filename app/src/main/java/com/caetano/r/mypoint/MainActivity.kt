package com.caetano.r.mypoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    private lateinit var userManager : UserManager

    companion object {
        private val LOADING = 0
        private val LOGIN = 1
        private val LOGGED = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManager = UserManager.newInstance(this)

        setContentView(R.layout.activity_main)

        if (userManager.hasSavedUser()) {
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
                if (response.isSuccessful && response.body() != null) {
                    arrangeLayout(LOGGED)
                    userManager.saveUser(response.body()!!)
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