package cz.mendelu.xmusil5.plantdiscoverer.communication

data class CommunicationError(
    val code: Int,
    var message: String?,
    var secondaryMessage: String? = null)