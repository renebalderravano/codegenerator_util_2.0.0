import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator';
import { AppService } from 'src/app.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule, AppFloatingConfigurator],
    template: `
        <app-floating-configurator />
        <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-screen overflow-hidden">
            <div class="flex flex-col items-center justify-center">
                <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                    <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                        <div class="text-center mb-8">
                            
                            <svg viewBox="0 0 85 86" fill="none" xmlns="http://www.w3.org/2000/svg" class="mb-8 w-16 shrink-0 mx-auto">
                               <style type="text/css">
                                    .st0{fill:#77777A;}
                                    .st1{fill:#FF8300;}
                                </style>
                                <g>
                                    <path class="st0" d="M41.3,0.1c-4.5,0-8.1,3.6-8.1,8.1c0,4.5,3.6,8.1,8.1,8.1c13.7,0,24.9,11.1,24.9,24.9C66.1,54.8,55,66,41.3,66
                                        c-13.7,0-24.8-11.1-24.8-24.8c0-2.9,0.5-5.6,1.4-8.2c-6.2,0-11.5-3.8-13.7-9.1C1.7,29,0.3,34.9,0.3,41.1c0,22.6,18.4,41,41,41
                                        s41-18.4,41-41C82.3,18.5,63.9,0.1,41.3,0.1"/>
                                    <path class="st1" d="M26.7,17.5c0-4.7-3.8-8.5-8.5-8.5c-4.7,0-8.5,3.8-8.5,8.5c0,4.7,3.8,8.5,8.5,8.5C22.9,26,26.7,22.2,26.7,17.5"
                                        />
                                </g>
                            </svg>
                            <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Bienvenido a plan de pensiones</div>
                            <span class="text-muted-color font-medium">Inicia sesión para continuar</span>
                        </div>

                        <div>
                            <label for="email1" class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2">Correo</label>
                            <input pInputText id="email1" type="text" placeholder="Correo" class="w-full md:w-120 mb-8" [(ngModel)]="email" />

                            <label for="password1" class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2">Contraseña</label>
                            <p-password id="password1" [(ngModel)]="password" placeholder="Contraseña" [toggleMask]="true" styleClass="mb-4" [fluid]="true" [feedback]="false"></p-password>

                            <div class="flex items-center justify-between mt-2 mb-8 gap-8">
                                <div class="flex items-center">
                                    <p-checkbox [(ngModel)]="checked" id="rememberme1" binary class="mr-2"></p-checkbox>
                                    <label for="rememberme1">Recordarme</label>
                                </div>
                                <span class="font-medium no-underline ml-2 text-right cursor-pointer text-primary">Olvidaste tu contraseña?</span>
                            </div>
                            <p-button label="Iniciar sesión" styleClass="w-full" (onClick)="login()"></p-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class Login implements OnInit {
    email: string = '';

    password: string = '';

    checked: boolean = false;

    constructor(private router: Router, private appService: AppService) { }

    ngOnInit(): void {
        sessionStorage.removeItem('token')
    }

    login() {
        let res = this.appService.getToken(this.email, this.password)
        res.then(result => {
            sessionStorage.setItem('token', JSON.stringify(result.token))
            console.log(result);
            this.router.navigate(['/dashboard'])
        }).catch(error => {
            this.router.navigate(['/'])
        });
    }
}
