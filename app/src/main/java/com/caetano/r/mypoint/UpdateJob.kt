package com.caetano.r.mypoint

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import com.caetano.r.mypoint.CustomApplication.Companion.UPDATE_CHANNEL_ID
import com.caetano.r.mypoint.api.ApiGithub
import com.caetano.r.mypoint.api.GithubRelease
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class UpdateJob : JobService() {

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
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
        val compare = githubRelease.tag_name.compareTo(BuildConfig.VERSION_NAME, true)
        if (compare > 0) {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(githubRelease.html_url))

            val resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    browserIntent,
                    PendingIntent.FLAG_ONE_SHOT
            )
            val builder = NotificationCompat.Builder(this, UPDATE_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme))
                    .setContentTitle(getString(R.string.update_notification_title))
                    .setContentText(getString(R.string.update_notification_description, githubRelease.tag_name))
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder.build())
        }
    }
}