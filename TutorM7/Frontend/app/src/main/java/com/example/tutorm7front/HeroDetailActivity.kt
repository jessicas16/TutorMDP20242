package com.example.tutorm7front

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.tutorm7front.databinding.ActivityHeroDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import android.graphics.BitmapFactory

class HeroDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeroDetailBinding
    private lateinit var hero: HeroEntity
    private val viewModel: HeroDetailViewModel by viewModels()
    private lateinit var editHeroLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hero = intent.getSerializableExtra("hero") as HeroEntity

        setupView()
        setupObservers()
        setupEditLauncher()
        setupButtons()
    }

    private fun setupView() {
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
        viewModel.deleteResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Hero deleted", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Failed to delete hero", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.editResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Hero updated", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Failed to update hero", Toast.LENGTH_SHORT).show()
            }
        })
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
            val intent = Intent(this, EditHeroActivity::class.java)
            intent.putExtra("hero", hero)
            editHeroLauncher.launch(intent)
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteHero(hero.id)
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
