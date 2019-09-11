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

package com.sygic.maps.uikit.viewmodels.common.sdk.search.map

import com.sygic.maps.uikit.viewmodels.common.utils.getStreetWithHouseNumberAndCityWithPostal
import com.sygic.maps.uikit.views.common.utils.TextHolder
import com.sygic.maps.uikit.views.searchresultlist.data.SearchResultItem
import com.sygic.sdk.search.MapSearchResult

abstract class MapSearchResultItem : SearchResultItem<MapSearchResult> {

    @get:MapSearchResult.DataType
    abstract val type: Int

    override val subTitle: TextHolder
        get() = TextHolder.from(
            getStreetWithHouseNumberAndCityWithPostal(
                dataPayload.street.text,
                dataPayload.addressPoint.text,
                dataPayload.city.text,
                dataPayload.postal.text
            )
        )
}