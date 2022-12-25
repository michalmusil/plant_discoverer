package cz.mendelu.xmusil5.plantdiscoverer.communication

import retrofit2.Response

interface IBaseRemoteRepository {
    fun <T: Any> processResponse(call: Response<T>): CommunicationResult<T> {
        try {
            if (call.isSuccessful) {
                call.body()?.let {
                    return CommunicationResult.Success(it)
                } ?: kotlin.run {
                    return CommunicationResult.Error(
                        CommunicationError(
                            call.code(),
                            call.errorBody().toString()
                        )
                    )
                }
            } else {
                return CommunicationResult.Error(
                    CommunicationError(
                        call.code(),
                        call.errorBody().toString()
                    )
                )
            }

        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }
    }

    fun <T: Any> processEmptyResponse(call: Response<T>): CommunicationResult<String> {
        if (call.isSuccessful) {
            return CommunicationResult.Success("")
        } else {
            return CommunicationResult.Error(
                CommunicationError(
                    call.code(),
                    call.errorBody().toString()
                )
            )
        }
    }
}