package com.example.pokemonregions.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.view.View
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
import com.example.pokemonregions.ui.viewmodel.RegionViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
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

    private val badgeColor = TypedValue()

    private val badgeDrawable by lazy {
        theme.resolveAttribute(com.google.android.material.R.attr.colorError,badgeColor,true)
        BadgeDrawable.create(this).apply {
            isVisible = true
            backgroundColor = badgeColor.data
            number = 0
        }
    }

    private val pokemonAdapter = PokemonsAdapter{
        if(viewModel.cannotAddPokemon()){
            Snackbar.make(binding.root,getString(R.string.pokemons_by_team),Snackbar.LENGTH_LONG)
                .setBackgroundTint(badgeColor.data)
                .setTextColor(ContextCompat.getColor(this,R.color.white))
                .show()
        }else{
            viewModel.selectPokemon(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initAdapter()
        initButtons()
        getData()
        observe()
    }

    private fun initButtons() {
        binding.regionSaveTeamButton.setOnClickListener{

        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initToolbar() {
        BadgeUtils.attachBadgeDrawable(badgeDrawable,binding.regionToolbar, R.id.teams_option_menu)
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
                    badgeDrawable.number = it.size
                    if(it.isNotEmpty()){
                        Snackbar.make(binding.root,getString(R.string.pokemon_added),Snackbar.LENGTH_LONG).show()
                    }
                    showTeamButton(it.size == 6)
                }
            }
        }
    }

    private fun showTeamButton(show: Boolean) {
        binding.regionSaveTeamButton.isVisible = show
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