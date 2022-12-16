package com.example.pokemonregions.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.pokemonregions.R
import com.example.pokemonregions.data.model.Pokemon
import com.example.pokemonregions.data.model.PokemonTeam
import com.example.pokemonregions.databinding.ActivityDetailTeamBinding
import com.example.pokemonregions.ui.adapter.PokemonsAdapter
import com.example.pokemonregions.ui.adapter.TeamsAdapter
import com.example.pokemonregions.ui.viewmodel.DetailTeamViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTeamActivity : AppCompatActivity() {

    private val errorColor = TypedValue()
    private lateinit var team: PokemonTeam

    private val binding by lazy { ActivityDetailTeamBinding.inflate(layoutInflater) }

    private val warningIcon by lazy { ContextCompat.getDrawable(this,R.drawable.ic_baseline_error_24) }
    private val successIcon by lazy { ContextCompat.getDrawable(this,R.drawable.ic_baseline_check_circle_24) }

    private val viewModel:DetailTeamViewModel by viewModels()

    private val adapter = PokemonsAdapter(true){pokemon,isDelete ->
        if(isDelete){
            removePokemon(pokemon)
        }
    }

    private fun removePokemon(pokemon: Pokemon) {
        if(team.pokemons.size <= 3){
            Snackbar.make(binding.root,getString(R.string.min_pokemons_on_update),Snackbar.LENGTH_LONG)
                .setBackgroundTint(errorColor.data)
                .show()
            return
        }
        team.pokemons.remove(pokemon)
        adapter.submitList(team.pokemons)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initColor()
        initData()
        initViews()
    }

    private fun initColor() {
        theme.resolveAttribute(com.google.android.material.R.attr.colorError,errorColor,true)
    }

    private fun initViews() {
        setSupportActionBar(binding.editToolbar)
        adapter.submitList(team.pokemons)
        binding.editPokemonsRecyclerview.adapter = adapter
        binding.editNameInput.editText!!.setText(team.name)
        binding.editToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.editSaveButton.setOnClickListener{
            team.name = binding.editNameInput.editText!!.text.toString()
            viewModel.updateTeam(team)
            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.success))
            .setMessage(getString(R.string.team_updated))
            .setIcon(successIcon)
            .setNeutralButton(getString(R.string.ok)){ d,_->
                d.dismiss()
                onBackPressed()
            }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.delete_menu_action ->{
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.warning))
                .setIcon(warningIcon)
                .setMessage(getString(R.string.delete_confirmation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)){_,_ ->
                    viewModel.deleteTeam(team)
                    onBackPressed()
                }
                .setNegativeButton(getString(R.string.no)){v,_ ->
                    v.dismiss()
                }.show()
            true
        }else -> super.onOptionsItemSelected(item)
    }

    private fun initData() {
        val extras = intent.extras!!
        team = extras.getSerializable(TeamsAdapter.TeamsViewHolder.DETAIL) as PokemonTeam
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu,menu)
        return true
    }

}