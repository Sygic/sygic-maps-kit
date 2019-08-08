/*
 * Copyright (c) 2019 Sygic a.s. All rights reserved.
 *
 * This project is licensed under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sygic.maps.uikit.viewmodels.navigation.signpost

import androidx.lifecycle.MutableLiveData
import com.sygic.maps.tools.annotations.AutoFactory
import com.sygic.maps.uikit.viewmodels.common.extensions.getNaviSignInfoOnRoute
import com.sygic.maps.uikit.viewmodels.common.extensions.pictogramDrawableRes
import com.sygic.maps.uikit.viewmodels.common.extensions.roadSigns
import com.sygic.maps.uikit.viewmodels.common.regional.RegionalManager
import com.sygic.maps.uikit.viewmodels.common.sdk.holders.NaviSignInfoHolder
import com.sygic.maps.uikit.viewmodels.common.utils.createInstructionText
import com.sygic.maps.uikit.views.common.extensions.combineLatest
import com.sygic.maps.uikit.views.navigation.roadsign.data.RoadSignData
import com.sygic.maps.uikit.views.navigation.signpost.FullSignpostView
import com.sygic.sdk.navigation.NavigationManager
import com.sygic.sdk.navigation.warnings.NaviSignInfo

private const val EMPTY_PICTOGRAM = 0

/**
 * A [FullSignpostViewModel] is a basic ViewModel implementation for the [FullSignpostView] class. It listens to
 * the Sygic SDK [NavigationManager.OnDirectionListener] and [NavigationManager.OnNaviSignListener] and updates the
 * distance, primaryDirection, secondaryDirection, secondaryDirectionText, instructionText, pictogram and roadSigns
 * in the [FullSignpostView].
 */
@AutoFactory
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class FullSignpostViewModel internal constructor(
    regionalManager: RegionalManager,
    private val navigationManager: NavigationManager
) : BaseSignpostViewModel(regionalManager, navigationManager), NavigationManager.OnNaviSignListener {

    val pictogram: MutableLiveData<Int> = MutableLiveData(EMPTY_PICTOGRAM)
    val roadSigns: MutableLiveData<List<RoadSignData>> = MutableLiveData(listOf())

    private val naviSignInfoHolder: MutableLiveData<NaviSignInfoHolder> = MutableLiveData(NaviSignInfoHolder.empty)

    init {
        navigationManager.addOnNaviSignListener(this)
        directionInfo.combineLatest(naviSignInfoHolder)
            .observeForever { instructionText.value = createInstructionText(it) }
    }

    override fun onNaviSignChanged(naviSignInfoList: List<NaviSignInfo>) {
        naviSignInfoList.getNaviSignInfoOnRoute().let {
            naviSignInfoHolder.value = NaviSignInfoHolder.from(it)
            roadSigns.value = it?.roadSigns() ?: listOf()
            pictogram.value = it?.pictogramDrawableRes() ?: EMPTY_PICTOGRAM
        }
    }

    override fun onCleared() {
        super.onCleared()

        navigationManager.removeOnNaviSignListener(this)
    }
}