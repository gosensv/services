package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.ServiceToStart

interface ResolveServiceEnableUseCase {
    operator fun<T : ServiceToStart> invoke(service: T): T
}
