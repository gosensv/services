package com.chrrissoft.services.foreground.usecases.classes

import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServiceEnableUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServicesEnableUseCase
import javax.inject.Inject

class ResolveServicesEnableUseCaseImpl @Inject constructor(
    private val ResolveServiceEnableUseCase: ResolveServiceEnableUseCase,
) : ResolveServicesEnableUseCase {
    override fun <T : ServiceToStart> invoke(services: List<T>): List<T> {
        return services.map { service ->
            ResolveServiceEnableUseCase(service)
        }
    }
}
