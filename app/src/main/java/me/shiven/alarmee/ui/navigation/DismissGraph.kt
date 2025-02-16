package me.shiven.alarmee.ui.navigation

import kotlinx.serialization.Serializable

//Routes objects//

sealed class DismissGraph {
    @Serializable
    data object DismissAppRoute : DismissGraph()

    @Serializable
    data object QRAppRoute : DismissGraph()

    @Serializable
    data object NFCAppRoute : DismissGraph()

    @Serializable
    data object GridAppRoute : DismissGraph()
}


