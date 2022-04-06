package lol.xget.groceryapp

import android.app.AppComponentFactory
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import dagger.hilt.android.HiltAndroidApp
import lol.xget.groceryapp.presentation.auth.register_user.RegisterUserViewModel

@HiltAndroidApp
class GroceryApp : Application()
