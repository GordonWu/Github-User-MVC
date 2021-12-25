package gordon.lab.github_user_mvc.data.result

import android.text.TextUtils
import com.google.gson.Gson
import gordon.lab.github_user_mvc.config.Setting
import gordon.lab.github_user_mvc.data.TaskHttpRequest
import gordon.lab.github_user_mvc.data.model.UserDetail
import gordon.lab.github_user_mvc.data.model.UserList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GithubApiTest {


    private fun getApiUri(path: String): String {
        if (TextUtils.isEmpty(path))
            throw NullPointerException("Parameter path cannot be null or empty string")

        return String.format("%s%s", Setting.SERVER_HOST, path)
    }

    private fun runGetUserListApi(lastId :Int, resp :StringBuilder) = runBlocking {
        TaskHttpRequest
            .newRequest(getApiUri("/users"))
            .method(TaskHttpRequest.Method.GET)
            .setArg("since",lastId.toString())
            .syncCall(resp)
    }

    @Test
    fun testGetUserList(){
        var data: UserList? = null
        val resp = StringBuilder()
        var status = runGetUserListApi(0, resp)
        data = Gson().fromJson(resp.toString(), UserList::class.java)
        //test first page
        Assert.assertEquals(200, status)
        assert( data.size == 30)
        println("first page first id::${data[0].id}")
        assert( data[0].id == 1)

        //test second page
        resp.clear()
        status = runGetUserListApi(data[data.size -1 ].id, resp)
        data = Gson().fromJson(resp.toString(), UserList::class.java)

        Assert.assertEquals(200, status)
        assert(data.size == 30)
        println("second page first id::${data[0].id}")
        assert(data[0].id != 1)
    }

    @Test
    fun testGetUserDetail(){
        var data: UserDetail? = null
        val resp = StringBuilder()
        val status = runBlocking {
             TaskHttpRequest
                .newRequest(getApiUri("/users/GordonWu"))
                .method(TaskHttpRequest.Method.GET)
                .syncCall(resp)
        }
        data = Gson().fromJson(resp.toString(), UserDetail::class.java)
        Assert.assertEquals(200, status)
        assert(data.login == "GordonWu")
        assert(data.id == 6059222)
        assert(data.location == "Taiwan. Taipei")
     }
}