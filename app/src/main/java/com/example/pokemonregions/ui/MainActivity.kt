package com.example.pokemonregions.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.pokemonregions.R
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.databinding.ActivityMainBinding
import com.example.pokemonregions.ui.adapter.RegionsAdapter
import com.example.pokemonregions.ui.view.RegionActivity
import com.example.pokemonregions.ui.viewmodel.MainViewModel
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val badgeColor = TypedValue()

    private val errorDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.error_title_dialog))
            .setMessage(getString(R.string.error_default_text_dialog))
            .setNegativeButton(getString(R.string.ok)){ view, _ ->
                view.dismiss()
                closeApp()
            }
            .create()
    }

    private val badgeDrawable by lazy {
        theme.resolveAttribute(com.google.android.material.R.attr.colorError,badgeColor,true)
        BadgeDrawable.create(this).apply {
            isVisible = true
            backgroundColor = badgeColor.data
            number = 0
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){ res ->
        this.onSignInResult(res)
    }

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

    }

    private val regionsAdapter = RegionsAdapter{
        val intent = Intent(this, RegionActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(RegionsAdapter.REGION_SELECTED,it)
        intent.putExtras(bundle)
        activityResult.launch(intent)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )

    private val authLayout = AuthMethodPickerLayout
        .Builder(R.layout.auth_custom_layout)
        .setGoogleButtonId(R.id.google_login_button)
        .setFacebookButtonId(R.id.facebook_login_button)
        .build()

    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.Theme_PokemonRegions)
        .setAvailableProviders(providers)
        .setAuthMethodPickerLayout(authLayout)
        .build()

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        if(result == null){
            return
        }
        val response = result.idpResponse
        if(response == null){
            viewModel.showErrorDialog()
        }else{
            if (result.resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser ?: return
                lifecycleScope.launch{
                    viewModel.userExists(user).collect{
                        when(it.status){

                            Result.Status.LOADING ->{

                            }

                            Result.Status.SUCCESS ->{
                                if(it.data != null){
                                    if(it.data){
                                        viewModel.setUserLogged(user)
                                    }else{
                                        viewModel.createUser(user)
                                    }
                                }
                            }

                            Result.Status.ERROR ->{
                                viewModel.showErrorDialog()
                            }
                        }
                    }
                }
                setContentView(binding.root)
            }else{
                viewModel.showErrorDialog(result.idpResponse?.error?.message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_start_view)
        signInLauncher.launch(signInIntent)
        initRecycler()
        observeLiveData()
    }

    private fun initRecycler(){
        binding.regionsLayout.regionsRecyclerView.adapter = regionsAdapter
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun observeLiveData(){
        viewModel.errorDialog.observe(this){ e ->
            e.getContentIfNotHandled().let{
                if(it == null){
                    errorDialog.show()
                }else{
                    errorDialog.setMessage(it)
                    errorDialog.show()
                }
            }
        }

        viewModel.regions.observe(this){
            when(it.status){
                Result.Status.LOADING ->{
                    showEmptyState()
                }

                Result.Status.SUCCESS ->{
                    regionsAdapter.submitList(it.data)
                    showRegions()
                }

                Result.Status.ERROR ->{
                    viewModel.showErrorDialog(it.error?.message)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.user.collect{
                if(it == null){
                    withContext(Dispatchers.Main){
                        setContentView(R.layout.loading_start_view)
                    }
                }else{
                    withContext(Dispatchers.Main){
                        BadgeUtils.attachBadgeDrawable(badgeDrawable,binding.homeToolbar,R.id.teams_option_menu)
                        setContentView(binding.root)
                    }
                }
            }
        }
    }

    private fun showEmptyState() {
        binding.regionsLayout.root.visibility = View.GONE
        binding.emptyHomeState.root.visibility = View.VISIBLE
    }

    private fun showRegions(){
        binding.regionsLayout.root.visibility = View.VISIBLE
        binding.emptyHomeState.root.visibility = View.GONE
    }

    private fun closeApp() {
        moveTaskToBack(false)
        exitProcess(0)
    }

}