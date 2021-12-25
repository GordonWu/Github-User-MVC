package gordon.lab.github_user_mvc.custom.ui.userdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import gordon.lab.github_user_mvc.R
import gordon.lab.github_user_mvc.core.FragmentBase
import gordon.lab.github_user_mvc.data.result.ResultUserDetail
import gordon.lab.github_user_mvc.data.task.TaskGetUserDetail
import gordon.lab.github_user_mvc.databinding.FragmentUserDetailBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserDetailFragment: FragmentBase() {

    private var binding: FragmentUserDetailBinding? = null
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.username.let {
            TaskGetUserDetail(it).exec()
            binding?.progressBar?.isVisible = true
        }

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetUserDetailResult(event: ResultUserDetail) {
        binding?.run{
            progressBar.isVisible = false
            Glide.with(this@UserDetailFragment)
                .load(event.data?.avatarUrl)
                .circleCrop()
                .into(imgAvatar)

            tvUserLogin.text = String.format(getString(R.string.str_detail_login_placeholder), event.data?.login)
            tvUserName.text = event.data?.name
            tvUserEmail.text = event.data?.email
            tvUserLocation.text = event.data?.location?:getString(R.string.str_detail_location_unset)
            tvUserCompany.text = event.data?.company?:getString(R.string.str_detail_company_unset)
            tvUserBio.text = event.data?.bio?:getString(R.string.str_detail_bio_unset)
            val url = event.data?.blog
            if(URLUtil.isValidUrl(url)){
                linkWrapper.isVisible = true
                tvUserLink.text = url
                tvUserLink.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
    }
}