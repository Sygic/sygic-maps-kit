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

package com.sygic.maps.module.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sygic.maps.module.common.delegate.ModulesComponentDelegate
import com.sygic.maps.module.common.initialization.manager.SdkInitializationManager
import com.sygic.maps.module.search.component.SearchFragmentInitComponent
import com.sygic.maps.module.search.databinding.LayoutSearchBinding
import com.sygic.maps.module.search.di.DaggerSearchComponent
import com.sygic.maps.module.search.viewmodel.SearchFragmentViewModel
import com.sygic.maps.tools.viewmodel.factory.ViewModelFactory
import com.sygic.sdk.position.GeoCoordinates
import javax.inject.Inject

/**
 * A *[SearchFragment]* is the core component for any search operation. It can be easily used to display search input
 * and search result list on the same screen. It can be modified with initial search input or coordinates. It comes with
 * several pre build-in elements such as [SearchInputEditText] or [SearchResultList]. TODO imports
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class SearchFragment : Fragment(), SdkInitializationManager.Callback {

    private val initComponent = SearchFragmentInitComponent()
    private val modulesComponent = ModulesComponentDelegate()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    internal lateinit var sdkInitializationManager: SdkInitializationManager

    private lateinit var fragmentViewModel: SearchFragmentViewModel

    private var injected = false
    private fun inject() {
        if (!injected) {
            DaggerSearchComponent.builder()
                .plus(modulesComponent.getInstance(this))
                .build()
                .inject(this)
            injected = true
        }
    }

    /**
     * If *[initialSearchInput]* is defined, then it will be processed immediately after initialization.
     *
     * @param [String] text input to be processed, null otherwise.
     *
     * @return [String] the initial text input value.
     */
    var initialSearchInput: String?
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.initialSearchInput.value
        } else initComponent.initialSearchInput
        set(value) {
            if (::fragmentViewModel.isInitialized) {
                fragmentViewModel.initialSearchInput.value = value
            } else initComponent.initialSearchInput = value
        }

    /**
     * If *[initialSearchPosition]* is defined, then it will be used for search result accuracy.
     *
     * @param [GeoCoordinates] search position to be used, null otherwise.
     *
     * @return [GeoCoordinates] the initial search position value.
     */
    var initialSearchPosition: GeoCoordinates?
        get() = if (::fragmentViewModel.isInitialized) {
            fragmentViewModel.initialSearchPosition.value
        } else initComponent.initialSearchPosition
        set(value) {
            if (::fragmentViewModel.isInitialized) {
                fragmentViewModel.initialSearchPosition.value = value
            } else initComponent.initialSearchPosition = value
        }

    override fun onInflate(context: Context, attrs: AttributeSet?, savedInstanceState: Bundle?) {
        inject()
        super.onInflate(context, attrs, savedInstanceState)
        initComponent.attributes = attrs
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        inject()
        sdkInitializationManager.initialize((context as Activity).application, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentViewModel =
            ViewModelProviders.of(this, viewModelFactory.with(initComponent))[SearchFragmentViewModel::class.java]

        lifecycle.addObserver(fragmentViewModel)
    }

    override fun onSdkInitialized() {
        // OnlineManager.getInstance().enableOnlineMapStreaming(true) //todo
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: LayoutSearchBinding = LayoutSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.searchFragmentViewModel = fragmentViewModel
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.removeObserver(fragmentViewModel)
    }
}
