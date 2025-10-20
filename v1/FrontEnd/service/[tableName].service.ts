import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CommonService } from '../common/common.service';

@Injectable({
  providedIn: 'root'
})
export class [tableName]Service extends CommonService {

  constructor(public http: HttpClient){    
    super(http,'[tableName]');
  }
  
}
