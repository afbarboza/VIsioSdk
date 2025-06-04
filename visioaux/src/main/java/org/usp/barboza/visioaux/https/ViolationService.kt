package org.usp.barboza.visioaux.https

import org.usp.barboza.visioaux.Violation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ViolationService {
  @POST("violation/{appId}")
  suspend fun sendViolation(
    @Path("appId") appId: String,
    @Body message: Violation
  ) : Response<ViolationResponse>
}