package gordon.lab.github_user_mvc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import gordon.lab.github_user_mvc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onBackPressed() {

        val navController = findNavController(R.id.fragmentContainer)
        if(!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}