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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE)

        val savedToken = sharedPref.getString(TOKEN, "")

        if (!TextUtils.isEmpty(savedToken)) {
            layout_login.visibility = View.GONE
            layout_loading.visibility = View.GONE
            layout_logged.visibility = View.VISIBLE
        }

        btn_login.setOnClickListener {
            layout_login.visibility = View.GONE
            layout_loading.visibility = View.VISIBLE

            val login = Login(edt_cpf.text.toString(), edt_password.text.toString())
            Api.service.signIn(login).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {

                        layout_loading.visibility = View.GONE
                        layout_logged.visibility = View.VISIBLE

                        val editor = sharedPref.edit()
                        editor.putString(CLIENT_ID, response.body().client_id)
                        editor.putString(TOKEN, response.body().token)
                        editor.putString(EMAIL, response.body().data.email)
                        editor.apply()
                    } else {
                        layout_loading.visibility = View.GONE
                        layout_login.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    layout_loading.visibility = View.GONE
                    layout_login.visibility = View.VISIBLE
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}