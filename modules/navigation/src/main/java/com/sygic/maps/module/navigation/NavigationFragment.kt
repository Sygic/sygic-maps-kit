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

package com.sygic.maps.module.navigation

import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sygic.maps.module.common.MapFragmentWrapper
import com.sygic.maps.module.navigation.component.DISTANCE_UNITS_DEFAULT_VALUE
import com.sygic.maps.module.navigation.component.PREVIEW_MODE_DEFAULT_VALUE
import com.sygic.maps.module.navigation.component.SIGNPOST_ENABLED_DEFAULT_VALUE
import com.sygic.maps.module.navigation.component.SIGNPOST_TYPE_DEFAULT_VALUE
import com.sygic.maps.module.navigation.databinding.LayoutNavigationBinding
import com.sygic.maps.module.navigation.di.DaggerNavigationComponent
import com.sygic.maps.module.navigation.di.NavigationComponent
import com.sygic.maps.module.navigation.types.SignpostType
import com.sygic.maps.module.navigation.viewmodel.NavigationFragmentViewModel
import com.sygic.maps.uikit.viewmodels.common.regional.RegionalManager
import com.sygic.maps.uikit.viewmodels.common.regional.units.DistanceUnits
import com.sygic.maps.uikit.viewmodels.navigation.signpost.FullSignpostViewModel
import com.sygic.maps.uikit.viewmodels.navigation.signpost.SimplifiedSignpostViewModel
import com.sygic.maps.uikit.views.common.extensions.getBoolean
import com.sygic.maps.uikit.views.common.extensions.getParcelableValue
import com.sygic.maps.uikit.views.navigation.signpost.FullSignpostView
import com.sygic.maps.uikit.views.navigation.signpost.SimplifiedSignpostView
import com.sygic.sdk.route.RouteInfo
import javax.inject.Inject

const val NAVIGATION_FRAGMENT_TAG = "navigation_map_fragment_tag"
internal const val KEY_DISTANCE_UNITS = "distance_units"
internal const val KEY_SIGNPOST_ENABLED = "signpost_enabled"
internal const val KEY_SIGNPOST_TYPE = "signpost_type"
internal const val KEY_PREVIEW_MODE = "preview_mode"
internal const val KEY_ROUTE_INFO = "route_info"

/**
 * A *[NavigationFragment]* is the core component for any navigation operation. It can be easily used for the navigation
 * purposes. By setting the [routeInfo] object will start the navigation process. Any pre build-in element such as
 * [FullSignpostView], [SimplifiedSignpostView], [Infobar], [CurrentSpeed] or [SpeedLimit] may be activated or deactivated and styled.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class NavigationFragment : MapFragmentWrapper<NavigationFragmentViewModel>() {

    override lateinit var fragmentViewModel: NavigationFragmentViewModel

    @Inject
    internal lateinit var regionalManager: RegionalManager

    override fun executeInjector() =
        injector<NavigationComponent, NavigationComponent.Builder>(DaggerNavigationComponent.builder()) { it.inject(this) }

    /**
     * A *[distanceUnits]* defines all available [DistanceUnits] type.
     *
     * [DistanceUnits.KILOMETERS] (default) -> Kilometers/meters are used as the distance unit.
     *
     * [DistanceUnits.MILES_YARDS] -> Miles/yards are used as the distance unit.
     *
     * [DistanceUnits.MILES_FEETS] -> Miles/feets are used as the distance unit.
     */
    var distanceUnits: DistanceUnits
        get() = if (::fragmentViewModel.isInitialized) {
            regionalManager.distanceUnits.value!!
        } else arguments.getParcelableValue(KEY_DISTANCE_UNITS) ?: DISTANCE_UNITS_DEFAULT_VALUE
        set(value) {
            arguments = Bundle(arguments).apply { putParcelable(KEY_DISTANCE_UNITS, value) }
            if (::fragmentViewModel.isInitialized) {
                regionalManager.distanceUnits.value = value
            }
        }

    /**
     * A *[signpostEnabled]* modifies the SignpostView ([FullSignpostView] or [SimplifiedSignpostView]) visibility.
     * Note, this need to be set before the [NavigationFragment] commit transaction.
     *
     * @param [Boolean] true to enable the SignpostView, false otherwise.
     *
     * @return whether the SignpostView is on or off.
     */
    var signpostEnabled: Boolean
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.signpostEnabled
        } else arguments.getBoolean(KEY_SIGNPOST_ENABLED, SIGNPOST_ENABLED_DEFAULT_VALUE)
        set(value) {
            arguments = Bundle(arguments).apply { putBoolean(KEY_SIGNPOST_ENABLED, value) }
        }

    /**
     * A *[signpostType]* defines the SignpostView ([FullSignpostView] or [SimplifiedSignpostView]) type to be used.
     * Note, this need to be set before the [NavigationFragment] commit transaction.
     *
     * @param [SignpostType] FULL -> [FullSignpostView], SIMPLIFIED -> [SimplifiedSignpostView].
     *
     * @return the current signpostView type value.
     */
    var signpostType: SignpostType
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.signpostType
        } else arguments.getParcelableValue(KEY_SIGNPOST_TYPE) ?: SIGNPOST_TYPE_DEFAULT_VALUE
        set(value) {
            arguments = Bundle(arguments).apply { putParcelable(KEY_SIGNPOST_TYPE, value) }
        }

    /**
     * A *[previewMode]* modifies whether the preview mode is on or off.
     *
     * @param [Boolean] true to enable the [previewMode], false otherwise.
     *
     * @return whether the [previewMode] is on or off.
     */
    var previewMode: Boolean
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.previewMode.value!!
        } else arguments.getBoolean(KEY_PREVIEW_MODE, PREVIEW_MODE_DEFAULT_VALUE)
        set(value) {
            arguments = Bundle(arguments).apply { putBoolean(KEY_PREVIEW_MODE, value) }
            if (::fragmentViewModel.isInitialized) {
                fragmentViewModel.previewMode.value = value
            }
        }

    /**
     * If not-null *[routeInfo]* is defined, then it will be used as an navigation routeInfo.
     *
     * @param [RouteInfo] route info object to be processed.
     *
     * @return [RouteInfo] the current route info value or `null` if not yet defined.
     */
    var routeInfo: RouteInfo?
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.routeInfo.value
        } else arguments.getParcelableValue(KEY_ROUTE_INFO)
        set(value) {
            arguments = Bundle(arguments).apply { putParcelable(KEY_ROUTE_INFO, value) }
            if (::fragmentViewModel.isInitialized && value != null) {
                fragmentViewModel.routeInfo.value = value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentViewModel = viewModelOf(NavigationFragmentViewModel::class.java, arguments)

        lifecycle.addObserver(fragmentViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = LayoutNavigationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.navigationFragmentViewModel = fragmentViewModel

        binding.signpostViewViewStub.setOnInflateListener { _, view ->
            DataBindingUtil.bind<ViewDataBinding>(view)?.let {
                it.lifecycleOwner = this
                it.setVariable(
                    BR.signpostViewModel, when (view) {
                        is FullSignpostView -> viewModelOf(FullSignpostViewModel::class.java)
                        is SimplifiedSignpostView -> viewModelOf(SimplifiedSignpostViewModel::class.java)
                        else -> throw IllegalArgumentException("Unknown view in the SignpostView viewStub.")
                    }
                )
            }
        }

        val root = binding.root as ViewGroup
        super.onCreateView(inflater, root, savedInstanceState)?.let {
            root.addView(it, 0)
        }
        return root
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.removeObserver(fragmentViewModel)
    }

    override fun resolveAttributes(attributes: AttributeSet) {
        with(requireContext().obtainStyledAttributes(attributes, R.styleable.NavigationFragment)) {
            if (hasValue(R.styleable.NavigationFragment_sygic_navigation_distanceUnits)) {
                distanceUnits = DistanceUnits.atIndex(
                    getInt(
                        R.styleable.NavigationFragment_sygic_navigation_distanceUnits,
                        DISTANCE_UNITS_DEFAULT_VALUE.ordinal
                    )
                )
            }
            if (hasValue(R.styleable.NavigationFragment_sygic_signpost_enabled)) {
                signpostEnabled =
                    getBoolean(
                        R.styleable.NavigationFragment_sygic_signpost_enabled,
                        SIGNPOST_ENABLED_DEFAULT_VALUE
                    )
            }
            if (hasValue(R.styleable.NavigationFragment_sygic_signpost_type)) {
                signpostType = SignpostType.atIndex(
                    getInt(
                        R.styleable.NavigationFragment_sygic_signpost_type,
                        SIGNPOST_TYPE_DEFAULT_VALUE.ordinal
                    )
                )
            }
            if (hasValue(R.styleable.NavigationFragment_sygic_navigation_previewMode)) {
                previewMode =
                    getBoolean(
                        R.styleable.NavigationFragment_sygic_navigation_previewMode,
                        PREVIEW_MODE_DEFAULT_VALUE
                    )
            }

            recycle()
        }
    }
}
