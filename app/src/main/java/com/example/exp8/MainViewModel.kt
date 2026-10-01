package com.example.exp8

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val initialDestinations = listOf(
        Destination(
            id = 1,
            name = "Taj Mahal",
            location = "Agra, Uttar Pradesh",
            description = "An immense mausoleum of white marble built by Shah Jahan in memory of Mumtaz Mahal.",
            isLocal = true,
            drawableResId = R.drawable.taj_mahal,
            webUrl = "https://en.wikipedia.org/wiki/Taj_Mahal"
        ),
        Destination(
            id = 2,
            name = "Hawa Mahal",
            location = "Jaipur, Rajasthan",
            description = "Iconic five-story Palace of Winds constructed with red and pink sandstone.",
            isLocal = true,
            drawableResId = R.drawable.hawa_mahal,
            webUrl = "https://en.wikipedia.org/wiki/Hawa_Mahal"
        ),
        Destination(
            id = 3,
            name = "Red Fort",
            location = "Delhi",
            description = "Historic fortified palace in Shahjahanabad showcasing Mughal architecture.",
            isLocal = true,
            drawableResId = R.drawable.red_fort,
            webUrl = "https://en.wikipedia.org/wiki/Red_Fort"
        ),
        Destination(
            id = 4,
            name = "Gateway of India",
            location = "Mumbai, Maharashtra",
            description = "Triumphal arch monument built to commemorate the landing of King George V.",
            isLocal = true,
            drawableResId = R.drawable.gateway_of_india,
            webUrl = "https://en.wikipedia.org/wiki/Gateway_of_India"
        ),
        Destination(
            id = 5,
            name = "Mysore Palace",
            location = "Mysuru, Karnataka",
            description = "Grand royal residence of the Wadiyar dynasty in Indo-Saracenic style.",
            isLocal = true,
            drawableResId = R.drawable.mysore_palace,
            webUrl = "https://en.wikipedia.org/wiki/Mysore_Palace"
        ),
        Destination(
            id = 6,
            name = "Charminar",
            location = "Hyderabad, Telangana",
            description = "16th-century mosque and monument featuring four grand minarets.",
            isLocal = true,
            drawableResId = R.drawable.charminar,
            imageUrl = "https://images.unsplash.com/photo-1627894483216-2138af692e32?fm=jpg&q=80&w=1080",
            webUrl = "https://en.wikipedia.org/wiki/Charminar"
        ),
        Destination(
            id = 7,
            name = "India Gate",
            location = "Delhi",
            description = "War memorial dedicated to Indian soldiers of the First World War.",
            isLocal = false,
            imageUrl = "https://commons.wikimedia.org/wiki/Special:FilePath/India_Gate,_New_Delhi.jpg",
            webUrl = "https://en.wikipedia.org/wiki/India_Gate"
        ),
        Destination(
            id = 8,
            name = "Qutub Minar",
            location = "Delhi",
            description = "73-metre high minaret forming part of the Qutb complex UNESCO site.",
            isLocal = true,
            drawableResId = R.drawable.qutub_minar,
            imageUrl = "https://images.unsplash.com/photo-1605649487212-47bdab064df7?fm=jpg&q=80&w=1080",
            webUrl = "https://en.wikipedia.org/wiki/Qutb_Minar"
        ),
        Destination(
            id = 9,
            name = "Konark Sun Temple",
            location = "Odisha",
            description = "13th-century Sun Temple shaped like a gigantic stone chariot with carved wheels.",
            isLocal = false,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/47/Konarka_Temple.jpg",
            webUrl = "https://en.wikipedia.org/wiki/Konark_Sun_Temple"
        ),
        Destination(
            id = 10,
            name = "Ajanta Caves",
            location = "Maharashtra",
            description = "Ancient rock-cut Buddhist cave monuments famous for exquisite mural paintings.",
            isLocal = false,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c3/Ajanta_%2863%29.jpg",
            webUrl = "https://en.wikipedia.org/wiki/Ajanta_Caves"
        )
    )

    private val _destinations = MutableLiveData<List<Destination>>(initialDestinations)
    val destinations: LiveData<List<Destination>> = _destinations

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _activeWebUrl = MutableLiveData<String>("https://www.google.com")
    val activeWebUrl: LiveData<String> = _activeWebUrl

    private val _selectedDestination = MutableLiveData<Destination?>(null)
    val selectedDestination: LiveData<Destination?> = _selectedDestination

    private var areAllSelected = false

    fun toggleFavourite(destinationId: Int) {
        val currentList = _destinations.value ?: return
        val updated = currentList.map { item ->
            if (item.id == destinationId) {
                item.copy(isFavourite = !item.isFavourite)
            } else {
                item
            }
        }
        _destinations.value = updated
    }

    fun toggleSelectAll(): Boolean {
        areAllSelected = !areAllSelected
        val currentList = _destinations.value ?: return areAllSelected
        val updated = currentList.map { item ->
            item.copy(isSelected = areAllSelected)
        }
        _destinations.value = updated
        return areAllSelected
    }

    fun toggleSelection(destinationId: Int) {
        val currentList = _destinations.value ?: return
        val updated = currentList.map { item ->
            if (item.id == destinationId) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        _destinations.value = updated
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openInWeb(url: String) {
        _activeWebUrl.value = url
    }

    fun setSelectedDestination(destination: Destination?) {
        _selectedDestination.value = destination
    }

    fun resetGallery() {
        areAllSelected = false
        _searchQuery.value = ""
        val currentList = _destinations.value ?: return
        _destinations.value = currentList.map { it.copy(isSelected = false) }
    }
}