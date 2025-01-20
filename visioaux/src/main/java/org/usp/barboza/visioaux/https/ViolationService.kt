package org.usp.barboza.visioaux.https

import org.usp.barboza.visioaux.Violation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ViolationService {
  @POST("violation")
  suspend fun sendViolation(@Body message: Violation) : Response<ViolationResponse>
}