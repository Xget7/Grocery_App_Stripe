package lol.xget.groceryapp.homeUser.domain

data class UserName(
    val value: String
) {
    fun validateNameLength() : Boolean {
        if (value.length < 4){
            return false
        }
        return true
    }

    fun validateMayus() : Boolean{
        if(value.matches(Regex("(?=.*[A-Z]).*"))){
           return false
        }
        return true
    }

}