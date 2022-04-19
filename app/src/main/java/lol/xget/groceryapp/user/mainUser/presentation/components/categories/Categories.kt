package lol.xget.groceryapp.user.mainUser.presentation.components.categories

enum class Categories(val value: String) {

    FRUITS("Fruits"),
    VEGETABLES("Vegetables"),
    BEVERAGES("Beverages"),
    BUTCHER("Butcher"),
    SNACKS("Snacks"),
    ALCOHOL("Alcohol"),
    BABY("Baby"),
    BREAKFAST("Breakfast"),
    FROZEN_FOOD("Frozen Food"),
    DAIRY("Dairy"),
    BEAUTY_PERSONAL_CARE("Beuty & Personal Care"),
    COOKING_NEEDS("Cooking Needs")
}


fun getAllFoodCategories(): List<Categories> {
    return listOf(
        Categories.FRUITS,
        Categories.VEGETABLES,
        Categories.BEVERAGES,
        Categories.BUTCHER,
        Categories.SNACKS,
        Categories.ALCOHOL,
        Categories.BABY,
        Categories.BREAKFAST,
        Categories.FROZEN_FOOD,
        Categories.DAIRY,
        Categories.BEAUTY_PERSONAL_CARE,
        Categories.COOKING_NEEDS,
    )
}

fun getFoodCategory(value : String): Categories?{
    val map = Categories.values().associateBy(Categories::value)
    return map[value]
}