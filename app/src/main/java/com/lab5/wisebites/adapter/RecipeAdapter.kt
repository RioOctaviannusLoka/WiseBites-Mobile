package com.lab5.wisebites.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lab5.wisebites.R
import com.lab5.wisebites.RecipeActivity
import com.lab5.wisebites.model.Recipe

class RecipeAdapter(
    private val context: Context,
    private val recipes: MutableList<Recipe>,
//    private val onBookmarkClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> (){
    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val recipeName: TextView = view.findViewById(R.id.tv_recipe_name)
        private val recipeCategory: TextView = view.findViewById(R.id.tv_recipe_tags)
        private val recipeArea: TextView = view.findViewById(R.id.tv_recipe_style)
        private val recipeImage:ImageView = view.findViewById(R.id.iv_recipe)
        private val bookmarkButton:ImageButton = view.findViewById(R.id.iv_recipe_bookmark)

        fun bind(recipe: Recipe) {
            recipeName.text = recipe.strMeal
            recipeCategory.text = recipe.strCategory
            recipeArea.text = recipe.strArea
            Glide.with(itemView.context).load((recipe.strMealThumb)).into(recipeImage)

            bookmarkButton.isSelected = recipe.isBookmarked

            itemView.setOnClickListener {
                val intent = Intent(context, RecipeActivity::class.java).apply {
                    putExtra("RECIPE_DATA", recipe)
                }
                context.startActivity(intent)
            }

            bookmarkButton.setOnClickListener {
                recipe.isBookmarked = !recipe.isBookmarked
                bookmarkButton.isSelected = recipe.isBookmarked
                notifyItemChanged(adapterPosition)
//                onBookmarkClick(recipe)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipes, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }
}