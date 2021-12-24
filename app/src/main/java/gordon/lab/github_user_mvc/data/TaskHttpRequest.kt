package gordon.lab.github_user_mvc.data

import android.util.Log
import gordon.lab.github_user_mvc.config.Setting
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class TaskHttpRequest (private val mUrl: String) {

    // 避免俺生過多的 OkHttpClient instance
    private var sClient: OkHttpClient? = null
    private val httpClient: OkHttpClient by lazy {
        if (null == sClient) {
            sClient = OkHttpClient.Builder()
                .connectTimeout(Setting.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Setting.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Setting.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

        return@lazy sClient!!
    }

    companion object {
        fun newRequest(url: String): TaskHttpRequest {
            return TaskHttpRequest(url)
        }
    }

    interface HttpCallback {
        fun onCompleted(statusCode: Int, resultBody: String?)
    }

    enum class Method {
        GET, POST, PUT, DELETE, PATCH, HEAD
    }

    private var mReq: Request.Builder = Request.Builder()
    private var mReqMethod: Method = Method.GET
    private var mReqBody: String? = null
    private var mReqBodyContentType: String = "application/x-www-form-urlencoded"
    private var mQueryStrings: ArrayList<String> = ArrayList()

    fun method(method: Method): TaskHttpRequest {
        mReqMethod = method
        return this
    }

    fun getArg(key: String, value: String): TaskHttpRequest {
        mQueryStrings.add("%s=%s".format(key, URLEncoder.encode(value, "utf-8")))
        return this
    }

    fun body(body: String, contentType: String = "application/x-www-form-urlencoded"): TaskHttpRequest {
        mReqBody = body
        mReqBodyContentType = contentType
        return this
    }

    fun body(body: JSONObject): TaskHttpRequest {
        return body(body.toString(), "application/json; charset=utf-8")
    }

    fun body(body: JSONArray): TaskHttpRequest {
        return body(body.toString(), "application/json; charset=utf-8")
    }

    fun body(args: List<Pair<String, String>>): TaskHttpRequest {
        val encodedFormData = StringBuilder()
        for (i in 0..args.size) {
            encodedFormData.append(if (encodedFormData.isNotEmpty()) "&" else "")
            encodedFormData.append("%s=%s".format(args[i].first, URLEncoder.encode(args[i].second, "utf-8")))
        }

        return body(encodedFormData.toString(), "application/x-www-form-urlencoded")
    }

    fun header(name: String, value: String): TaskHttpRequest {
        mReq.header(name, value)
        return this
    }

    fun syncCall(resp: StringBuilder): Int {
        prepareInternal()

        return try {
            val response = httpClient.newCall(mReq.build()).execute()
            //進行解密
            if (200 == response.code && null != response.body) {
                resp.append(response.body?.string())
            }

            response.code
        } catch (ex: Exception) {
            Log.d("gw", "ex:$ex")
            0
        }
    }

    fun asyncCall(callback: HttpCallback) {
        prepareInternal()

        httpClient.newCall(mReq.build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onCompleted(response.code,  response.body?.string())
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("TaskHttpRequest", e.localizedMessage)
                callback.onCompleted(0, null)
            }
        })
    }

    private fun prepareInternal() {
        mReq.url(mUrl + if (mQueryStrings.size > 0) "?" + mQueryStrings.joinToString("&") else "")

        when (mReqMethod) {
            Method.GET -> mReq.get()
            Method.POST -> {
                mReqBody?.let {
                    mReq.post(it.toRequestBody(mReqBodyContentType.toMediaTypeOrNull()))
                }?:run{
                    throw NullPointerException("POST: No body content found")
                }
            }
            Method.DELETE -> {
                mReqBody?.let {
                    mReq.delete(it.toRequestBody(mReqBodyContentType.toMediaTypeOrNull()))
                }?:run{
                    mReq.delete()
                }
            }
            Method.HEAD -> mReq.head()
            Method.PATCH -> {
                mReqBody?.let {
                    mReq.patch(it.toRequestBody(mReqBodyContentType.toMediaTypeOrNull()))
                }?:run{
                    throw NullPointerException("PATCH: No body content found")
                }
            }
            Method.PUT -> {
                mReqBody?.let {
                     mReq.put(it.toRequestBody(mReqBodyContentType.toMediaTypeOrNull()))
                }?:run{
                    throw NullPointerException("PUT: No body content found")
                }
            }
        }

        // Release
        mReqBody = null
        mQueryStrings.clear()
    }
}