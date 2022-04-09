package lol.xget.groceryapp.homeUser.domain

class UserPassword(
    val value : String
) {

    fun verifyLength(): Boolean{
        if (value.length >= 6){
            return true
        }
        return false
    }

}