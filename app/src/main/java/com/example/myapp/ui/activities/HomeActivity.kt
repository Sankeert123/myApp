package com.example.myApp.ui.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.myApp.ui.fragments.AccountsFragment
import com.example.myApp.ui.fragments.GroupsFragment
import com.example.myApp.R
import com.example.myApp.databinding.ActivityHomeBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHomeBinding
    private val binding get() = _binding
    lateinit var addGrpsItem : MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Setting Tool bar
        binding.homeToolbar.title = ""
        setSupportActionBar(binding.homeToolbar)

        // Setting click listeners to tool bar elements
        binding.homeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.searchIcon -> {
                    val intent = Intent(this, SearchToolbarActivity::class.java)
                    startActivity(intent)
                }

                R.id.add_groups -> {
                    val intent = Intent(this, StartNewGroupActivity::class.java)
                    startActivity(intent)
                }
            }
            return@setOnMenuItemClickListener true

        }

        // Initialising Firebase notification login
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d("token toast", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })



        // Setting the default fragment
        val grpFragment = GroupsFragment()
        val accountsFragment = AccountsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.home_frame_layout, grpFragment).commit()


        // Setting the fragment on bottom navigation click
        binding.bottomNavLayout.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.itm_groups -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.home_frame_layout, grpFragment).commit()
                    addGrpsItem.isVisible = true

                }
                R.id.itm_account -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame_layout, accountsFragment).commit()
                    addGrpsItem.isVisible = false
                }
            }
            return@setOnItemSelectedListener true

        }

    }

    // Inflating menu in the tool bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_items, menu)
         addGrpsItem = binding.homeToolbar.menu.findItem(R.id.add_groups)
        return super.onCreateOptionsMenu(menu)
    }
}