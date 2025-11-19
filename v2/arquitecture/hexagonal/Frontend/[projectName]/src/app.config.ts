import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import Aura from '@primeuix/themes/aura';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import { CustomInterceptor } from '@/core/guards/custom-interceptor';

export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(
            appRoutes, 
            withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }), 
            withEnabledBlockingInitialNavigation()),
            provideHttpClient(withInterceptorsFromDi()
        ),
        { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true },
        provideAnimationsAsync(),
        providePrimeNG({ 
            theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } 
        })
    ]
};
