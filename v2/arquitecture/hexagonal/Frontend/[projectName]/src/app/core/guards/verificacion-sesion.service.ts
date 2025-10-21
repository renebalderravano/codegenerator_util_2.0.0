/**
 * verificacion-sesion.service.ts  1.0 16/03/18
 *
 * Copyright (c) Centro para el Desarrollo de la Industria
 * de Software.
 */


import {map} from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {
  CanActivate, CanActivateChild, ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';
  import {jwtDecode} from 'jwt-decode';
/**
 * Contiene los métodos para detectar si un usuario ha iniciado sesión
 * antes de mostrar cualquier link.
 *
 * @version 1.0 16/03/18
 * @author Evert Cruz Reyes
 */
@Injectable(
  {
    providedIn: 'root'
  }
)
export class VerificacionSesionService implements CanActivate, CanActivateChild {

  private loginURL = '';
  private token: any ='';

  constructor ( private router: Router) { }

  /**
   * Revisa el estado se la sesión antes de mostrar el link requerido
   */
  canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ):
    boolean | Observable<boolean> | Promise<boolean> {

    return this.checkTokenExpiration(this.getToken()).pipe(map(
      ( value ) => {
        if ( value ) {
          return true;
        }

        this.router.navigate( [ this.loginURL ] );
        return false;
      } ));

  }

  /**
   * Revisa el estado se la sesión antes de mostrar los links hijos requeridos
   */
  canActivateChild( childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot ):
    boolean | Observable<boolean> | Promise<boolean> {

    return this.checkTokenExpiration(this.getToken()).pipe(map(
      ( value ) => {
        if ( value ) {
          return true;
        }

        this.router.navigate( [ this.loginURL ] );
        return false;
      } ));

  }
   getToken() {
        this.token = sessionStorage.getItem('token');
        this.token = JSON.parse(this.token)
        return this.token;
    }

    checkTokenExpiration(token: string):  Observable<boolean> {
      try {
        const decodedToken: any = jwtDecode(token);
        const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
        return of( currentTime < decodedToken.exp); // Returns true if expired
      } catch (error) {
        console.error('Invalid token:', error);
        return of(false); // Treat invalid tokens as expired
      }
    }
    

}
