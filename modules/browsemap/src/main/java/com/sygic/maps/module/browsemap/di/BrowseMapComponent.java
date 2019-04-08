package com.sygic.maps.module.browsemap.di;

import com.sygic.maps.module.browsemap.BrowseMapFragment;
import com.sygic.maps.module.browsemap.di.module.ViewModelModule;
import com.sygic.maps.module.common.di.ModulesComponent;
import com.sygic.maps.module.common.di.util.ModuleBuilder;
import dagger.Component;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope
@Retention(RetentionPolicy.RUNTIME)
@interface Browse { }

@Browse
@Component(
        modules = {
                ViewModelModule.class
        },
        dependencies = {
                ModulesComponent.class
        }
)
public interface BrowseMapComponent {
    @Component.Builder
    abstract class Builder implements ModuleBuilder<BrowseMapComponent> {}

    void inject(BrowseMapFragment fragment);
}