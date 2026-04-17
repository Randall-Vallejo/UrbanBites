package urbanbites.com.presentation.state

data class ProfileState(
    val isLoading: Boolean = false,
    val userName: String = "User"
)

sealed interface ProfileEffect {
    data object NavigateToEdit : ProfileEffect
}
