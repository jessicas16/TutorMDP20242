package com.example.tutorm7front

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorm7front.databinding.ActivityHeroDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import android.graphics.BitmapFactory
import android.util.Log

class HeroDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeroDetailBinding
    private var hero: HeroEntity? = null
    private val viewModel: HeroDetailViewModel by viewModels()
    private lateinit var editHeroLauncher: ActivityResultLauncher<Intent>
    private lateinit var heroId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        heroId = intent.getStringExtra("heroId") ?: ""

        setupObservers()
        setupEditLauncher()

        if (heroId.isNotEmpty()) {
            viewModel.fetchHeroById(heroId)
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setupView(hero: HeroEntity) {
        binding.textViewName.text = hero.name
        binding.textViewDescription.text = hero.description
        binding.textViewDifficulty.text = "Difficulty: ${"🌟".repeat(hero.difficulty.toInt())}"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(hero.image)
                val input = url.openStream()
                val bitmap = BitmapFactory.decodeStream(input)

                runOnUiThread {
                    binding.imageViewHero.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupObservers() {
//        Semua observers ditaruh sini
        viewModel.hero.observe(this) { fetchedHero ->
            hero = fetchedHero
            setupView(fetchedHero)
            setupButtons() // <-- setup buttons AFTER hero is ready
        }

        viewModel.deleteResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Hero deleted", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Failed to delete hero", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.editResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Hero updated", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Failed to update hero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditLauncher() {
        editHeroLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupButtons() {
        binding.buttonEdit.setOnClickListener {
            hero?.let { nonNullHero ->
                val intent = Intent(this, EditHeroActivity::class.java)
                intent.putExtra("hero", nonNullHero)
                editHeroLauncher.launch(intent)
            }
        }

        binding.buttonDelete.setOnClickListener {
            hero?.let { nonNullHero ->
                viewModel.deleteHero(nonNullHero.id)
            }
        }
    }
}
