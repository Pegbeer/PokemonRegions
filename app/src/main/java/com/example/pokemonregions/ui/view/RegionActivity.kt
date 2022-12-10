package com.example.pokemonregions.ui.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonregions.databinding.ActivityRegionBinding
import com.example.pokemonregions.ui.MainActivity
import com.example.pokemonregions.ui.adapter.RegionsAdapter
import com.example.pokemonregions.ui.viewmodel.RegionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegionActivity : AppCompatActivity() {

    private val viewModel:RegionViewModel by viewModels()

    private var regionSelected:String? = null

    private val binding by lazy { ActivityRegionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        val extras = intent.extras
        if(extras != null){
            regionSelected = extras.getString(RegionsAdapter.REGION_SELECTED)
            regionSelected?.let {
                binding.name = it
            }
        }
    }

}