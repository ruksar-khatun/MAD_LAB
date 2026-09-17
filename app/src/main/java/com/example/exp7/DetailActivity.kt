package com.example.exp7

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val recipe = intent.getSerializableExtra("recipe") as? Recipe
        if (recipe != null) {
            findViewById<View>(R.id.detailContent).visibility = View.VISIBLE
            
            findViewById<TextView>(R.id.recipeDetailName).text = recipe.name
            findViewById<TextView>(R.id.recipeDetailOrigin).text = recipe.origin
            findViewById<TextView>(R.id.recipeDescription).text = recipe.description
            findViewById<TextView>(R.id.recipeIngredients).text = recipe.ingredients
            findViewById<TextView>(R.id.recipeInstructions).text = recipe.instructions
            
            // Loading local drawable resource via Glide
            Glide.with(this)
                .load(recipe.imageRes)
                .placeholder(R.drawable.rounded_indicator)
                .error(R.drawable.rounded_indicator)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(findViewById<ImageView>(R.id.recipeDetailImage))
        }
    }
}
