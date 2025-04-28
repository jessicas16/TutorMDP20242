package com.example.tutorm7front

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorm7front.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var heroDetailLauncher: ActivityResultLauncher<Intent>
    private val viewModel: HeroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupHeroDetailLauncher()
        setupObservers()

        viewModel.fetchHeroes()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupHeroDetailLauncher() {
        heroDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.fetchHeroes() // Refresh after editing or deleting
            }
        }
    }

    private fun setupObservers() {
        viewModel.heroes.observe(this, Observer { heroes ->
            binding.recyclerView.adapter = HeroAdapter(heroes) { hero ->
                val intent = Intent(this, HeroDetailActivity::class.java)
                intent.putExtra("heroId", hero.id)
                heroDetailLauncher.launch(intent)
            }
        })

        viewModel.error.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        })
    }
}
