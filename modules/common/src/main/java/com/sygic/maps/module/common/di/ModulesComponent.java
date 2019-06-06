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

package com.sygic.maps.module.common.di;

import android.app.Application;
import com.sygic.maps.module.common.di.module.*;
import com.sygic.maps.module.common.initialization.manager.SdkInitializationManager;
import com.sygic.maps.module.common.mapinteraction.manager.MapInteractionManager;
import com.sygic.maps.module.common.poi.manager.PoiDataManager;
import com.sygic.maps.module.common.theme.ThemeManager;
import com.sygic.maps.uikit.viewmodels.common.location.LocationManager;
import com.sygic.maps.uikit.viewmodels.common.permission.PermissionsManager;
import com.sygic.maps.uikit.viewmodels.common.sdk.model.ExtendedCameraModel;
import com.sygic.maps.uikit.viewmodels.common.sdk.model.ExtendedMapDataModel;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
                AppModule.class,
                MapModule.class,
                PoiDataManagerModule.class,
                SdkInitializationManagerModule.class,
                PermissionsModule.class,
                LocationModule.class,
                ThemeModule.class
        }
)
public interface ModulesComponent {
    ExtendedMapDataModel getMapDataModel();
    ExtendedCameraModel getCameraModel();
    MapInteractionManager getMapInteractionManager();
    PoiDataManager getPoiDataManager();
    SdkInitializationManager getSdkInitializationManager();
    PermissionsManager getPermissionsManager();
    LocationManager getLocationManager();
    ThemeManager getThemeManager();
    Application getApplication();
}