package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.ServiceToStart


interface ResolveServicesEnableUseCase {
    operator fun<T : ServiceToStart> invoke(services: List<T>): List<T>
}

