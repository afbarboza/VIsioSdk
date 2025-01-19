package org.usp.barboza.visioaux.https

class ViolationRepository {
    private var violationService: ViolationService = RetrofitInstance
        .getRetrofit()
        .create(ViolationService::class.java)

    suspend fun reportViolation(message: TmpMessage) = violationService.sendViolation(message)
}