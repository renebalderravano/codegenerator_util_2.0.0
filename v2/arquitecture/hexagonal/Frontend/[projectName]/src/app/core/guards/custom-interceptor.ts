import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { catchError, firstValueFrom, Observable, of, throwError } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {

    token: any = '';

    private excludedUrls = [
        "http://localhost:8080/api/auth/login"
    ];

    constructor(private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const shouldSkip = this.excludedUrls.some(url => req.url.includes(url));

        if (shouldSkip) {
            return next.handle(req); // 🚫 Skip token injection
        }

        // Modify the request (e.g., add headers, change URL)
//        const modifiedReq = req.clone({
//            setHeaders: {
//                'Authorization': `Bearer ` + this.getToken(), // Example: Add an Authorization header
//                'Access-Control-Allow-Origin': 'https://localhost:4200',
//                'Access-Control-Allow-Credentials': 'true'
//            },
//        });

        // console.log('Intercepted Request:', modifiedReq);
        // Pass the modified request to the next handler
        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    // 🔁 Redirect to login page
                    this.router.navigate(['/auth/login']);
                }
                return throwError(() => error);
            })
        );
    }

    getToken() {
        this.token = sessionStorage.getItem('token');
        this.token = JSON.parse(this.token)
        return this.token;
    }



    checkTokenExpiration(token: string) {
        try {
            const decodedToken: any = jwtDecode(token);
            const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
            return currentTime < decodedToken.exp; // Returns true if expired
        } catch (error) {
            console.error('Invalid token:', error);
            return false; // Treat invalid tokens as expired
        }
    }

}
