package com.example.pokemonregions.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pokemonregions.databinding.ActivityTeamsBinding
import com.example.pokemonregions.ui.adapter.TeamsAdapter
import com.example.pokemonregions.ui.viewmodel.TeamsViewModel
import com.example.pokemonregions.core.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TeamsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTeamsBinding.inflate(layoutInflater) }

    private val viewModel:TeamsViewModel by viewModels()

    private val teamsAdapter = TeamsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        initObservers()
        loadData()
    }

    private fun loadData() {
        viewModel.getTeams()
    }

    private fun initViews() {
        binding.teamsRecyclerView.adapter = teamsAdapter
        binding.teamsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.teams.collect{
                when(it.status){
                    Result.Status.LOADING ->{
                        showLoadingState()
                    }
                    Result.Status.ERROR ->{
                        showEmptyState()
                        viewModel.showErrorDialog(it.error?.message)
                    }
                    Result.Status.SUCCESS ->{
                        teamsAdapter.submitList(it.data!!)
                        if(it.data.isNotEmpty()){
                            showReadyState()
                        }else{
                            showEmptyState()
                        }
                    }
                }
            }
        }
    }

    private fun showReadyState() {
        with(binding){
            teamsRecyclerView.visibility = View.VISIBLE
            teamsEmptyState.root.visibility = View.GONE
            teamsLoadingState.root.visibility = View.GONE
        }
    }

    private fun showEmptyState() {
        with(binding){
            teamsRecyclerView.visibility = View.GONE
            teamsEmptyState.root.visibility = View.VISIBLE
            teamsLoadingState.root.visibility = View.GONE
        }
    }

    private fun showLoadingState() {
        with(binding){
            teamsRecyclerView.visibility = View.GONE
            teamsEmptyState.root.visibility = View.GONE
            teamsLoadingState.root.visibility = View.VISIBLE
        }
    }


}