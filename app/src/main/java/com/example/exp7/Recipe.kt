package com.example.exp7

import java.io.Serializable

data class Recipe(
    val name: String,
    val origin: String,
    val description: String, // New field for detailed overview
    val ingredients: String,
    val instructions: String,
    val color: Int,
    val imageRes: Int
) : Serializable {

    companion object {
        fun getData(): List<Recipe> {
            return listOf(
                Recipe(
                    "Hyderabadi Biryani",
                    "Hyderabad, India",
                    "A world-famous regal dish from Hyderabad, known for its aromatic basmati rice, tender marinated meat, and a perfect blend of spices cooked in the traditional 'Dum' style. It is considered the epitome of Indian culinary excellence.",
                    "• 500g Basmati rice\n• 700g Goat meat or Chicken\n• 1 cup thick Yogurt\n• 2 cups Fried onions (Birista)\n• Saffron-infused milk\n• Pure Ghee\n• Whole spices (Cardamom, Cloves, Cinnamon)\n• Fresh Mint & Cilantro",
                    "1. Marinate the meat with yogurt, ginger-garlic paste, and spices for at least 4 hours.\n2. Par-boil the rice with whole spices until it is 70% cooked.\n3. Layer the marinated meat in a heavy-bottom pot, followed by rice, fried onions, and saffron milk.\n4. Seal with dough and cook on 'Dum' (slow steam) for 45-60 minutes.",
                    android.graphics.Color.parseColor("#FF9933"),
                    R.drawable.img
                ),
                Recipe(
                    "Butter Chicken",
                    "Delhi, India",
                    "Also known as Murgh Makhani, this is a creamy, mildly spiced tomato-based curry. It originated in the 1950s in Delhi and has since become one of the most popular Indian dishes globally.",
                    "• 500g Chicken Tikka\n• 2 cups Tomato puree\n• 1/2 cup Heavy cream\n• 50g Unsalted Butter\n• 1 tbsp Kasuri Methi\n• Ginger-garlic paste\n• Honey or Sugar for balance",
                    "1. Grill marinated chicken pieces until smoky and tender.\n2. Prepare a rich gravy by simmering tomato puree with butter and aromatic spices.\n3. Add the grilled chicken to the gravy and simmer on low heat.\n4. Finish with a generous drizzle of heavy cream and crushed dried fenugreek leaves.",
                    android.graphics.Color.parseColor("#E64A19"),
                    R.drawable.img_1
                ),
                Recipe(
                    "Masala Dosa",
                    "South India",
                    "A staple of South Indian cuisine, the Masala Dosa is a thin, crispy crepe made from fermented rice and lentil batter, filled with a delicious spiced potato mash. Best served with sambar and coconut chutney.",
                    "• Fermented rice and lentil batter\n• 3 Boiled potatoes\n• 1 tsp Mustard seeds\n• 10-12 Curry leaves\n• 1/2 tsp Turmeric powder\n• Sliced onions & Green chilies",
                    "1. Sauté onions, mustard seeds, turmeric, and curry leaves, then add mashed potatoes to make the filling.\n2. Pour a ladle of batter onto a hot griddle and spread it into a very thin circle.\n3. Drizzle oil around the edges until the dosa becomes golden and crispy.\n4. Place the potato filling in the center, fold, and serve hot.",
                    android.graphics.Color.parseColor("#FBC02D"),
                    R.drawable.img_2
                ),
                Recipe(
                    "Palak Paneer",
                    "North India",
                    "A nutritious and vibrant green curry made with fresh spinach puree and cubes of Indian cottage cheese (Paneer). It is a classic vegetarian dish loved for its mild flavor and creamy texture.",
                    "• 500g Fresh spinach (Palak)\n• 200g Paneer cubes\n• 1 tsp Cumin seeds\n• 4 cloves Garlic, minced\n• 1 tsp Garam Masala\n• 2 tbsp Fresh Cream",
                    "1. Blanch the spinach leaves and blend into a smooth puree.\n2. Sauté minced garlic and cumin in oil, then add the spinach puree and spices.\n3. Add the paneer cubes (pan-fry them first for extra texture) and simmer for 5 minutes.\n4. Stir in a dash of cream before serving with hot Rotis.",
                    android.graphics.Color.parseColor("#388E3C"),
                    R.drawable.img_3
                ),
                Recipe(
                    "Gulab Jamun",
                    "Pan-India",
                    "The most beloved Indian dessert, Gulab Jamuns are soft, spongy milk-solid balls deep-fried to a golden brown and soaked in a warm, cardamom-scented sugar syrup.",
                    "• 1 cup Khoya (Reduced milk solids)\n• 2 tbsp All-purpose flour\n• 2 cups Sugar for syrup\n• 3-4 Cardamom pods\n• Rose water or Saffron strands\n• Ghee for deep frying",
                    "1. Knead the khoya and flour into a smooth dough and form small, crack-free balls.\n2. Deep fry the balls in ghee on very low heat until they turn deep golden brown.\n3. Prepare a sugar syrup infused with cardamom and rose water.\n4. Soak the hot fried balls in the warm syrup for at least 2 hours until they double in size.",
                    android.graphics.Color.parseColor("#C62828"),
                    R.drawable.img_4
                ),
                Recipe(
                    "Paneer Tikka",
                    "Punjab, India",
                    "A popular appetizer where chunks of paneer are marinated in a spiced yogurt mixture and grilled in a Tandoor. It is known for its smoky charred edges and burst of flavors.",
                    "• 250g Paneer cubes\n• 1 Diced Capsicum & Onion\n• 1/2 cup Thick Yogurt\n• 1 tbsp Besan (Gram flour)\n• Ajwain, Red chili powder, & Chaat Masala",
                    "1. Mix yogurt, besan, and spices to create a thick marinade.\n2. Coat paneer cubes and veggies in the marinade and rest for 1 hour.\n3. Skewer the paneer and vegetables alternatingly.\n4. Grill or bake at high heat until the edges are slightly charred. Sprinkle with chaat masala.",
                    android.graphics.Color.parseColor("#FF6D00"),
                    R.drawable.img_5
                ),
                Recipe(
                    "Chole Bhature",
                    "Delhi/Punjab, India",
                    "A heavy and satisfying meal consisting of spicy chickpeas (Chole) and deep-fried leavened bread (Bhatura). A signature dish of North Indian street food culture.",
                    "• 1 cup Chickpeas (soaked overnight)\n• 2 cups All-purpose flour\n• 1/2 cup Yogurt for dough\n• Tea leaves (for dark chole color)\n• Amchur (Dried mango powder)",
                    "1. Pressure cook chickpeas with spices and a tea bag for a rich dark color.\n2. Cook the chickpeas in a spicy gravy until the oil separates.\n3. Knead a soft fermented dough for the bhaturas using flour and yogurt.\n4. Roll out thin discs and deep fry until they puff up like balloons.",
                    android.graphics.Color.parseColor("#795548"),
                    R.drawable.img_6
                ),
                Recipe(
                    "Pav Bhaji",
                    "Mumbai, India",
                    "The ultimate Mumbai street food. It is a thick vegetable mash cooked with a special spice blend, served with soft buttered bread rolls (Pav), lemon, and chopped onions.",
                    "• Mixed vegetables (Potato, Peas, Cauliflower)\n• 4 Soft Pav rolls\n• 100g Amul Butter\n• 2 tbsp Pav Bhaji Masala\n• 1 large Onion, finely chopped",
                    "1. Boil and mash the mixed vegetables until smooth.\n2. Sauté onions in a large amount of butter, add spices and the vegetable mash.\n3. Cook on a flat tawa, adding water and butter as needed to reach the desired consistency.\n4. Slit the pav rolls and toast them with plenty of butter before serving.",
                    android.graphics.Color.parseColor("#D84315"),
                    R.drawable.img_7
                ),
                Recipe(
                    "Dal Makhani",
                    "Punjab, India",
                    "A rich, buttery, and creamy lentil dish made from whole black urad dal and kidney beans. Traditionally slow-cooked for hours to achieve its signature velvety texture.",
                    "• 1 cup Whole Black Lentils (Urad)\n• 1/4 cup Kidney beans (Rajma)\n• 4 tbsp Butter\n• 1/2 cup Tomato puree\n• 1/4 cup Fresh cream\n• Ginger-garlic paste",
                    "1. Soak lentils and beans overnight, then pressure cook until very soft.\n2. In a pot, simmer the cooked lentils with tomato puree, ginger, and garlic for at least 30 minutes.\n3. Add a generous amount of butter and let it slow-cook to develop deep flavors.\n4. Finish with fresh cream and serve with Garlic Naan.",
                    android.graphics.Color.parseColor("#4E342E"),
                    R.drawable.img_8
                ),
                Recipe(
                    "Samosa Chat",
                    "North India",
                    "A tangy and spicy street food delight made by crushing crispy samosas and topping them with warm chickpea curry, chilled yogurt, and a variety of sweet and spicy chutneys.",
                    "• 2 Large Samosas\n• 1 cup Warm Chole (Chickpea curry)\n• 1/2 cup Whisked Yogurt\n• Tamarind & Mint Chutneys\n• Fine Sev & Fresh Cilantro",
                    "1. Place crispy samosas in a shallow bowl and crush them slightly.\n2. Pour a generous ladle of hot chickpea curry (chole) over them.\n3. Add layers of chilled yogurt followed by sweet tamarind and spicy mint chutneys.\n4. Garnish with a handful of fine sev, pomegranate seeds, and fresh cilantro leaves.",
                    android.graphics.Color.parseColor("#AFB42B"),
                    R.drawable.img_9
                )
            )
        }
    }
}
