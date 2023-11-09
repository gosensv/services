package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.Permission

interface ResolvePermissionsEnableUseCase {
    operator fun invoke(services: List<Permission>): List<Permission>
}
