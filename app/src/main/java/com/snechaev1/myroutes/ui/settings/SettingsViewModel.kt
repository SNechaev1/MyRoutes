package com.snechaev1.myroutes.ui.settings

import androidx.lifecycle.ViewModel
import com.snechaev1.myroutes.data.model.ApiResource
import com.snechaev1.myroutes.network.ApiDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//    @ApplicationContext context: Context,
//    private val dataRepository: DataRepository,
    private val apiDataSource: ApiDataSource
): ViewModel() {

    suspend fun setNotification(enable: Boolean): ApiResource<String> {
        return apiDataSource.setNotification(enable)
    }
}

//