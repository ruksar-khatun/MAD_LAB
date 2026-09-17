package com.example.exp7

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MainActivity : AppCompatActivity() {

    private var isTwoPane: Boolean = false
    private lateinit var heroImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Robust Visibility Fix: Dynamically adjust toolbar padding for status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            findViewById<View>(R.id.appBarLayout).setPadding(0, systemBars.top, 0, 0)
            insets
        }

        isTwoPane = findViewById<View>(R.id.detailContainer) != null
        val recipes = Recipe.getData()
        val adapter = RecipeAdapter(this, recipes)
        
        val listView = findViewById<ListView>(R.id.recipeListView)
        
        // Add Header: Hero Image Section
        val header = LayoutInflater.from(this).inflate(R.layout.layout_header_home, listView, false)
        heroImage = header.findViewById(R.id.heroImage)
        listView.addHeaderView(header)
        
        // Add Footer: Signature Section
        val footer = LayoutInflater.from(this).inflate(R.layout.layout_footer_signature, listView, false)
        listView.addFooterView(footer)
        
        listView.adapter = adapter

        // Hero Image: Authentic Indian Thali from your drawables (img_7)
        Glide.with(this)
            .load(R.drawable.img_7)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(heroImage)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Adjust position for header
            val adjustedPosition = position - listView.headerViewsCount
            if (adjustedPosition >= 0 && adjustedPosition < recipes.size) {
                val selectedRecipe = recipes[adjustedPosition]
                if (isTwoPane) {
                    updateDetailView(selectedRecipe)
                } else {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("recipe", selectedRecipe)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateDetailView(recipe: Recipe) {
        findViewById<View>(R.id.placeholderText)?.visibility = View.GONE
        findViewById<View>(R.id.detailContent)?.visibility = View.VISIBLE
        findViewById<TextView>(R.id.recipeDetailName)?.text = recipe.name
        findViewById<TextView>(R.id.recipeDetailOrigin)?.text = recipe.origin
        findViewById<TextView>(R.id.recipeDescription)?.text = recipe.description
        findViewById<TextView>(R.id.recipeIngredients)?.text = recipe.ingredients
        findViewById<TextView>(R.id.recipeInstructions)?.text = recipe.instructions
        val detailImageView = findViewById<ImageView>(R.id.recipeDetailImage)
        if (detailImageView != null) {
            Glide.with(this)
                .load(recipe.imageRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detailImageView)
        }
    }
}
