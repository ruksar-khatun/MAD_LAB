package com.example.exp7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RecipeAdapter(private val context: Context, private val dataSource: List<Recipe>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_recipe, parent, false)

        val nameTextView = rowView.findViewById<TextView>(R.id.recipeName)
        val originTextView = rowView.findViewById<TextView>(R.id.recipeOrigin)
        val imageView = rowView.findViewById<ImageView>(R.id.recipeImage)
        val colorStrip = rowView.findViewById<View>(R.id.recipeColorStrip)

        val recipe = getItem(position) as Recipe
        nameTextView.text = recipe.name
        originTextView.text = recipe.origin
        colorStrip.setBackgroundColor(recipe.color)
        
        // Loading local drawable resource via Glide as requested
        Glide.with(context)
            .load(recipe.imageRes)
            .placeholder(R.drawable.rounded_indicator)
            .error(R.drawable.rounded_indicator)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        return rowView
    }
}
