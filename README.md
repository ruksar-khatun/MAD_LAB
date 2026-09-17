# Exp 7: Indian Flavors - Adaptive Android Application

## Overview
**Indian Flavors** is a professional, visually appealing Android application developed as part of Experiment 7. The application showcases a curated list of 10 iconic Indian dishes, ranging from savory mains like Hyderabadi Biryani to beloved desserts like Gulab Jamun. 

The app implements a **Master-Detail pattern** that is fully **adaptive**, providing a optimized experience for both mobile phones and tablets. It leverages modern Android development practices, including Material 3 design, dynamic image loading via Glide, and robust window inset handling for edge-to-edge display.

## Core Experiments
- **Adaptive UI Design:** Implementing different layouts for various screen sizes (Phone vs. Tablet sw600dp) using a single codebase.
- **Master-Detail Pattern:** Managing navigation and data flow between a list-based homepage and a content-rich detail view.
- **Dynamic Image Loading:** Using the **Glide** library to fetch, cache, and display high-quality images from both local resources and online APIs.
- **Custom View Adapters:** Building a custom `BaseAdapter` to render complex `ListView` items with dynamic colors and icons.
- **Edge-to-Edge Integration:** Utilizing `WindowInsets` to ensure the UI respects system bars and notches for a modern look.
- **Networking with Retrofit:** Integrated infrastructure for real-time image searching using the Google Custom Search API.

## Code Snippet
### Dynamic Adaptive Logic (MainActivity.kt)
```kotlin
// Robust Inset Handling for clear Header visibility
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { _, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    findViewById<View>(R.id.appBarLayout).setPadding(0, systemBars.top, 0, 0)
    insets
}

// Handling Item Clicks based on Screen Configuration
listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
    val adjustedPosition = position - listView.headerViewsCount
    if (adjustedPosition >= 0 && adjustedPosition < recipes.size) {
        val selectedRecipe = recipes[adjustedPosition]
        if (isTwoPane) {
            updateDetailView(selectedRecipe) // In-place update for Tablets
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("recipe", selectedRecipe)
            startActivity(intent) // Navigation for Phones
        }
    }
}
```

## Folder Structure
```text
Exp7/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/exp7/
│   │   │   │   ├── MainActivity.kt        # Adaptive logic & UI management
│   │   │   │   ├── DetailActivity.kt      # Recipe detail display
│   │   │   │   ├── Recipe.kt              # Data model & Indian dish data
│   │   │   │   ├── RecipeAdapter.kt       # Custom ListView adapter
│   │   │   │   ├── GoogleSearchService.kt # API interface
│   │   │   │   └── GoogleSearchModels.kt  # API response models
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml  # Master layout
│   │   │   │   │   ├── activity_detail.xml # Detail layout
│   │   │   │   │   ├── list_item_recipe.xml # Row layout
│   │   │   │   │   ├── layout_header_home.xml # Hero section
│   │   │   │   │   └── layout_footer_signature.xml # Signature
│   │   │   │   ├── drawable/              # Local authentic dish images
│   │   │   │   └── values/                # Colors & Material 3 themes
│   │   │   └── AndroidManifest.xml        # Permissions & App config
├── README.md                              # Project documentation
└── build.gradle.kts                       # Dependency management
```

## Output & Screenshots

| Screenshot | Description |
| :--- | :--- |
| ![Homepage](./WhatsApp%20Image%202026-09-17%20at%2011.11.37%20PM.jpeg) | **Professional Homepage:** Displays the "Indian Flavors" title perfectly aligned below the status bar, followed by a stunning Indian Thali hero image. |
| ![Recipe List](./WhatsApp%20Image%202026-09-17%20at%2011.11.37%20PM%20(1).jpeg) | **Authentic Recipe List:** Shows the 10 Indian dishes with accurate thumbnails from local drawables and dynamic color strips. |
| ![Detail Overview](./WhatsApp%20Image%202026-09-17%20at%2011.11.37%20PM%20(2).jpeg) | **Recipe Detail (Overview):** When a dish is clicked, it shows a large authentic image and a professional "About this Dish" historical summary. |
| ![Ingredients & Steps](./WhatsApp%20Image%202026-09-17%20at%2011.11.38%20PM.jpeg) | **Ingredients & Instructions:** A deep dive into the recipe with precise measurements and step-by-step cooking instructions in Material cards. |
| ![Footer & Tablet](./WhatsApp%20Image%202026-09-17%20at%2011.11.38%20PM%20(1).jpeg) | **Adaptive Footer:** The signature "Ruksar Khatun | 25MCAR0115" footer perfectly aligned at the bottom of the content. |

---
**Developed by:** Ruksar Khatun  
**USN:** 25MCAR0115
