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

package com.sygic.samples.browsemap

import android.view.View
import androidx.appcompat.widget.MenuPopupWindow
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sygic.maps.module.common.mapinteraction.MapSelectionMode
import com.sygic.samples.R
import com.sygic.samples.base.map.BaseMapTest
import com.sygic.samples.browsemap.robot.browseMap
import com.sygic.samples.utils.MapMarkers
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowseMapModesEspressoTest : BaseMapTest(BrowseMapModesActivity::class.java) {

    @Test
    fun browseMapDisplayed() {
        browseMap(activity) {
            isViewNotDisplayed(R.id.compassView)
            isViewDisplayed(R.id.positionLockFab)
            isViewDisplayed(R.id.zoomControlsMenu)
        }
    }

    @Test
    fun selectionModes() {
        browseMap(activity) {
            onView(withId(R.id.selectionModeButton)).check(matches(isDisplayed()))
            clickOnMapToLocation(GeneralLocation.CENTER)
            isPlaceDetailHidden()
            clickOnMapToLocation(GeneralLocation.CENTER_RIGHT)
            isPlaceDetailHidden()
            clickOnMapMarker(MapMarkers.sampleMarkerOne)
            isPlaceDetailHidden()

            clickOnView(R.id.selectionModeButton)
            onData(CoreMatchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .inAdapterView(CoreMatchers.instanceOf<View>(MenuPopupWindow.MenuDropDownListView::class.java))
                .atPosition(MapSelectionMode.MARKERS_ONLY)
                .perform(click())

            clickOnMapToLocation(GeneralLocation.CENTER_LEFT)
            isPlaceDetailHidden()
            clickOnMapMarker(MapMarkers.sampleMarkerOne)
            isPlaceDetailVisible()
            pressBack()
            isPlaceDetailHidden()

            clickOnView(R.id.selectionModeButton)
            onData(CoreMatchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .inAdapterView(CoreMatchers.instanceOf<View>(MenuPopupWindow.MenuDropDownListView::class.java))
                .atPosition(MapSelectionMode.FULL)
                .perform(click())

            clickOnMapToLocation(GeneralLocation.CENTER_LEFT)
            isPlaceDetailVisible()
            pressBack()
            clickOnMapMarker(MapMarkers.sampleMarkerOne)
            isPlaceDetailVisible()
            pressBack()
            isPlaceDetailHidden()
        }
    }
}