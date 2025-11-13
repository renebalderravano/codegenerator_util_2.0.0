import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { firstValueFrom, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  
  public token: string = ''

  constructor(private http: HttpClient) {
    //this.getToken()
  }
  
  async getToken(username: string, password:string) {

    const httpOptions = {
      headers: new HttpHeaders({

        "Content-Type": "application/json",
        // "Content-Type" : "application/x-www-form-urlencoded",
        // "Authorization": 'Basic c3ByaW5nLXNlY3VyaXR5LW9hdXRoMi1yZWFkLXdyaXRlLWNsaWVudDpzcHJpbmctc2VjdXJpdHktb2F1dGgyLXJlYWQtd3JpdGUtY2xpZW50LXBhc3N3b3JkMTIzNA==',
        "Access-Control-Allow-Origin": "https://localhost:4200/"
		// "Authorization": 'Basic ' + btoa('spring-security-oauth2-read-write-client' + ':' + 'spring-security-oauth2-read-write-client-password1234')
      })
    };

    // const formData = new FormData();
    // formData.append('grant_type', 'password')
    // formData.append('username', username)
    // formData.append('password', password)
    // formData.append('client_id', 'spring-security-oauth2-read-write-client')

    var formData = {
    "username": username,
    "password": password
    }
    
    // return this.http.post('http://localhost:8080/oauth/token?username=admin&password=admin1234&grant_type=password', formData, httpOptions)
    let result: any =  await firstValueFrom(this.http.post('http://localhost:8080/api/auth/login', formData, { withCredentials: true }))
  //   .subscribe(
  //     (res: any)=>{
        this.token = result.token
        //sessionStorage.setItem('token', JSON.stringify(this.token))     
  //     },
  //     err=>{
  //       console.error(err);
        
  //     }
  //   )
  // )

  return result
  }

}
