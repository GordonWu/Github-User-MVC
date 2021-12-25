package gordon.lab.github_user_mvc.core

import android.app.Application

class GithubApp :Application() {
    companion object {
        lateinit var instance: GithubApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}