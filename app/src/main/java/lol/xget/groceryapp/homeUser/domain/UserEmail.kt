package lol.xget.groceryapp.homeUser.domain

data class UserEmail(
    var value : String
){

    fun verifyEmail() : Boolean{
        if (value.matches(Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))){
            return false
        }
        return true
    }
}