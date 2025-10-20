import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

export class CommonService {

    token: any = '';
    url_server_backend = 'http://localhost:8080'

    constructor(public http: HttpClient, public entity_name: string) {
        
    }

    getHeaders() {
        var headers = new HttpHeaders({
            'Content-type': 'application/json',
            'Authorization': 'Bearer ' + this.getToken(),
            'Access-Control-Allow-Origin': 'http://localhost:4200',
            'Access-Control-Allow-Credentials': 'true'
        })
        return headers
    }

    getToken() {
        this.token = sessionStorage.getItem('token');
        this.token = JSON.parse(this.token)
        return this.token;
    }


    save(data: any): Observable<any> {
        var headers = this.getHeaders()
        return this.http.post(this.url_server_backend + '/' + this.entity_name + '/save', data, { headers: headers })
    }

    findAll(): Observable<any> {
        var headers = this.getHeaders()
        return this.http.get(this.url_server_backend + '/' + this.entity_name + '/findAll', { headers: headers })
    }

    findById(id: number): Observable<any> {
        let headers = this.getHeaders()
        return this.http.get(this.url_server_backend + '/' + this.entity_name + '/findById/' + id, { headers: headers })
    }

}