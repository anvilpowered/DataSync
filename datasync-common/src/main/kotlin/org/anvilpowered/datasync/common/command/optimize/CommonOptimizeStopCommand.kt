/*
 *   DataSync - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.datasync.common.command.optimize

import com.google.inject.Inject
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.datasync.api.misc.LockService
import org.anvilpowered.datasync.api.plugin.PluginMessages
import org.anvilpowered.datasync.api.registry.DataSyncKeys
import org.anvilpowered.datasync.api.snapshotoptimization.SnapshotOptimizationManager

open class CommonOptimizeStopCommand<
    TString : Any,
    TUser : Any,
    TCommandSource : Any> {

    @Inject
    private lateinit var lockService: LockService

    @Inject
    private lateinit var permissionService: PermissionService

    @Inject
    private lateinit var pluginMessages: PluginMessages<TString>

    @Inject
    protected lateinit var registry: Registry

    @Inject
    private lateinit var snapshotOptimizationManager: SnapshotOptimizationManager<TUser, TString, TCommandSource>

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    fun execute(source: TCommandSource) {
        if (!permissionService.hasPermission(source, registry.getOrDefault(DataSyncKeys.MANUAL_OPTIMIZATION_BASE_PERMISSION))) {
            textService.send(pluginMessages.noPermissions, source)
            return
        }
        if (!lockService.assertUnlocked(source)) {
            return
        }
        textService.send(snapshotOptimizationManager.stop(), source)
    }
}
