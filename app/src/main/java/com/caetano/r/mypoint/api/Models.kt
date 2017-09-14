package com.caetano.r.mypoint.api

data class Login(val login: String, val password: String)

data class LoginData(val email: String)

data class LoginResponse(val token: String, val client_id: String, val data: LoginData)

data class Register(val time_card: TimeCard, val _path: String, val _device: Device,
                    val _appVersion: String)

data class TimeCard(val latitude: Double, val longitude: Double, val address: String,
                    val reference_id: String?, val original_latitude: Double,
                    val original_longitude: Double, val original_address: String,
                    val location_edited: Boolean, val accuracy: Int)

data class Device(val cordova: String, val manufacturer: String, val model: String,
                  val platform: String, val uuid: String?,
                  val version: String)

data class RegisterResponse(val success: String)