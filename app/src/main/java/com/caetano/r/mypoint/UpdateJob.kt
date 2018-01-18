package com.caetano.r.mypoint

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.caetano.r.mypoint.api.ApiGithub
import com.caetano.r.mypoint.api.GithubRelease
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class UpdateJob : JobService() {

    private val updateChannel = "UPDATE"

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        val mBuilder = NotificationCompat.Builder(this, updateChannel)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello World!")


        ApiGithub.service.getLatestRelease().enqueue(object : Callback<GithubRelease> {
            override fun onResponse(call: Call<GithubRelease>, response: Response<GithubRelease>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { notifyIfNeeded(it) }
                }
            }

            override fun onFailure(p0: Call<GithubRelease>?, p1: Throwable?) {
                Log.e("UpdateJob", p1?.message)
            }
        })
        return true
    }

    private fun notifyIfNeeded(githubRelease: GithubRelease) {
        if (BuildConfig.VERSION_NAME)
    }
}