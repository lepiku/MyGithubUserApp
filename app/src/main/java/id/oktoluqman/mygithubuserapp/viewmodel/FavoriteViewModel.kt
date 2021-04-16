package id.oktoluqman.mygithubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.oktoluqman.mygithubuserapp.model.GithubUser

class FavoriteViewModel : ViewModel() {
    private val listGithubUsers = MutableLiveData<List<GithubUser>>()

    fun setUsers(users: List<GithubUser>) {
        listGithubUsers.postValue(users)
    }

    fun getUsers(): LiveData<List<GithubUser>> {
        return listGithubUsers
    }
}