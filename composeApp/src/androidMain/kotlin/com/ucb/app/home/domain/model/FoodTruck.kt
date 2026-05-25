import kotlinx.serialization.Serializable

@Serializable
data class FoodTruck(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val distance: String = "",
    val promoText: String = "",
    val isPromo: Boolean = false,
    val isOpen: Boolean = true,
    val imageUrl: String = "" // Para fotos reales después
)