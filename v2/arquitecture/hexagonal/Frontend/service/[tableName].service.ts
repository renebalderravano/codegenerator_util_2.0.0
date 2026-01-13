import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CommonService } from '../common.service';

@Injectable({
  providedIn: 'root'
})
export class [tableName]Service extends CommonService {

  constructor(http: HttpClient){    
    super(http,'[tableName]');
  }
  
}
