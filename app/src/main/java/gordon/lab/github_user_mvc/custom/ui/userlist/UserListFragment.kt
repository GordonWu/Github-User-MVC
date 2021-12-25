package gordon.lab.github_user_mvc.custom.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import gordon.lab.github_user_mvc.core.FragmentBase
import gordon.lab.github_user_mvc.custom.adapter.UserListAdapter
import gordon.lab.github_user_mvc.data.model.UserItems
import gordon.lab.github_user_mvc.data.result.ResultUserList
import gordon.lab.github_user_mvc.data.task.TaskGetUserList
import gordon.lab.github_user_mvc.databinding.FragmentUserListBinding
import gordon.lab.github_user_mvc.util.NavControllerHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class UserListFragment: FragmentBase() {

    private var binding: FragmentUserListBinding?= null
    private val navHelper: NavControllerHelper = NavControllerHelper()
    private var userListAdapter = UserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initUserRecycler()
            if (userListAdapter.itemCount == 0){
                TaskGetUserList(0).exec()
                progressBar.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navHelper.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FragmentUserListBinding.initUserRecycler(){
        userList.adapter = userListAdapter
        userListAdapter.setOnBottomReachedListener(onUserListBottomReachedHandler)
        userListAdapter.setOnItemListListener(onUserListItemClickListener)
        userList.layoutManager = LinearLayoutManager(context)
        userList.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
    }

    private val onUserListBottomReachedHandler = object : UserListAdapter.OnBottomReachedListener {
        override fun onBottomReached(userItem: UserItems) {
            TaskGetUserList(userItem.id).exec()
            binding?.progressBar?.isVisible = true
        }
    }

    private val onUserListItemClickListener = object:UserListAdapter.OnItemListClickListener{
        override fun onClick(data: UserItems) {
            navHelper.navigate(findNavController(), UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(data.login))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetUserListResult(event: ResultUserList) {
        event.data?.let{
            userListAdapter.setDataModel(it)
            binding?.progressBar?.isVisible = false
        }
    }
}