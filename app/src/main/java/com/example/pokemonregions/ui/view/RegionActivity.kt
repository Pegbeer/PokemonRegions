package com.example.pokemonregions.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.pokemonregions.R
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.data.model.Region
import com.example.pokemonregions.databinding.ActivityRegionBinding
import com.example.pokemonregions.ui.adapter.PokemonsAdapter
import com.example.pokemonregions.ui.adapter.RegionsAdapter
import com.example.pokemonregions.ui.dialog.InputDialog
import com.example.pokemonregions.ui.viewmodel.RegionViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegionActivity : AppCompatActivity() {

    private val viewModel:RegionViewModel by viewModels()

    private var regionSelected:Region? = null

    private val binding by lazy { ActivityRegionBinding.inflate(layoutInflater) }

    private val errorColor = TypedValue()
    private val successColor = TypedValue()
    private val textColor = TypedValue()

    private lateinit var badge:BadgeDrawable

    private val pokemonAdapter = PokemonsAdapter{
        if(viewModel.cannotAddPokemon()){
            val snack = Snackbar.make(binding.root,getString(R.string.pokemons_by_team),Snackbar.LENGTH_LONG)
                .setBackgroundTint(errorColor.data)
                .setTextColor(ContextCompat.getColor(this,R.color.white))
            val layoutParams = (snack.view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }
            snack.view.layoutParams = layoutParams
            snack.show()
        }else{
            viewModel.selectPokemon(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initColors()
        initBadge()
        initToolbar()
        initAdapter()
        initButtons()
        getData()
        observe()
    }

    private fun initBadge() {
        badge = BadgeDrawable.create(this).apply {
            isVisible = true
            number = 0
            backgroundColor = errorColor.data
        }
    }

    private fun initColors() {
        theme.resolveAttribute(com.google.android.material.R.attr.colorError,errorColor,true)
        theme.resolveAttribute(com.google.android.material.R.attr.colorTertiaryContainer,successColor,true)
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnBackground,textColor,true)
    }

    private fun initButtons() {
        binding.regionSaveTeamButton.isEnabled = false
        binding.regionSaveTeamButton.setOnClickListener{
            if(viewModel.getSelectedCount() in 3..6){
                InputDialog(this)
                    .setTitle(getString(R.string.enter_name))
                    .setPositiveButton(getString(R.string.ok)){ name,view ->
                        viewModel.saveTeam(name.toString())
                        view.dismiss()
                        showStateCleared()
                    }.showDialog()
            }else{
                val snack = Snackbar.make(binding.root,getString(R.string.min_pokemons),Snackbar.LENGTH_LONG)
                val layoutParams = (snack.view.layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                }
                snack.view.layoutParams = layoutParams
                snack.show()
            }
        }
    }

    private fun showStateCleared() {
        badge.number = 0
        val snack = Snackbar
            .make(binding.root,getString(R.string.team_saved),Snackbar.LENGTH_LONG)
            .setBackgroundTint(successColor.data)
            .setTextColor(textColor.data)
        val layoutParams = (snack.view.layoutParams as FrameLayout.LayoutParams).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        snack.view.layoutParams = layoutParams
        snack.show()
        viewModel.clear()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initToolbar() {
        BadgeUtils.attachBadgeDrawable(badge,binding.regionToolbar, R.id.teams_option_menu)
        binding.regionToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initAdapter() {
        binding.regionPokemonsRecycler.adapter = pokemonAdapter
    }

    private fun observe() {
        lifecycleScope.launch(Dispatchers.IO){
            viewModel.pokemons.collect{
                withContext(Dispatchers.Main){
                    when(it.status){
                        Result.Status.LOADING ->{
                            showLoadingState()
                        }
                        Result.Status.ERROR ->{
                            showEmptyState()
                        }
                        Result.Status.SUCCESS ->{
                            pokemonAdapter.submitList(it.data)
                            showReadyState()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.pokemonsSelectedState.collect{
                withContext(Dispatchers.Main){
                    badge.number = it.size
                    if(it.isNotEmpty()){
                        Snackbar.make(binding.root,getString(R.string.pokemon_added),Snackbar.LENGTH_SHORT).show()
                    }
                    showTeamButton(it.size in 3..6)
                }
            }
        }
    }

    private fun showTeamButton(show: Boolean) {
        binding.regionSaveTeamButton.isEnabled = show
    }

    private fun showReadyState() {
        binding.regionEmptyView.root.visibility = View.GONE
        binding.regionLoadingView.root.visibility = View.GONE
        binding.regionPokemonsRecycler.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        binding.regionEmptyView.root.visibility = View.GONE
        binding.regionLoadingView.root.visibility = View.VISIBLE
        binding.regionPokemonsRecycler.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.regionEmptyView.root.visibility = View.VISIBLE
        binding.regionLoadingView.root.visibility = View.GONE
        binding.regionPokemonsRecycler.visibility = View.GONE
    }

    private fun getData() {
        val extras = intent.extras
        if(extras != null){
            regionSelected = extras.getSerializable(RegionsAdapter.REGION_SELECTED) as Region
            viewModel.getPokemons(regionSelected!!.generation)
        }
    }

}