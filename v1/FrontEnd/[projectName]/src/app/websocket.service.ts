import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppService } from './app.service';
// import * as Stomp from 'stompjs';
// import * as SockJS from 'sockjs-client';

// var SockJs = require("sockjs-client");
// var Stomp = require("stompjs");

declare var SockJS: any;
declare var Stomp: any;

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  constructor(private http: HttpClient, private appService: AppService) {

  }

  // Open connection with the back-end socket
  public connect() {
  let socket = new SockJS(`http://localhost:8080/socket?access_token=`+this.appService.token);
  let stompClient = Stomp.over(socket);

  // let stompClient = Stomp.client('http://localhost:8080/socket?access_token=7f459b68-0453-4df6-adb2-3654c5c4bf42')
 
    return stompClient;
  }

}
