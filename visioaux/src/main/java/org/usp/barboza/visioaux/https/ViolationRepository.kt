package org.usp.barboza.visioaux.https

import org.usp.barboza.visioaux.Violation

class ViolationRepository {
    private var violationService: ViolationService = RetrofitInstance
        .getRetrofit()
        .create(ViolationService::class.java)

    suspend fun reportViolation(violation: Violation) = violationService.sendViolation(violation)
}