package org.usp.barboza.visioaux.https

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ViolationService {
  @POST("message")
  suspend fun sendViolation(@Body message: TmpMessage) : Response<ViolationResponse>
}