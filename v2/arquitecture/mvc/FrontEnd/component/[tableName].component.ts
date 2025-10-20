import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule, CurrencyPipe, NgStyle, NgTemplateOutlet } from '@angular/common';

import {
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  CardImgDirective,
  ColComponent,
  RowComponent,
  TableDirective
} from '@coreui/angular';
import { CAMEL_CASE_CAP[tableName]Service } from '../../services/SNAKE_CASE[tableName].service';
import { ReactiveFormsModule } from '@angular/forms';
import { IconDirective } from '@coreui/icons-angular';

@Component({
  selector: 'app-SNAKE_CASE[tableName]s',
  standalone: true,
  imports: [CommonModule, IconDirective, ReactiveFormsModule, NgStyle, TableDirective, CardBodyComponent, CardHeaderComponent, CardComponent, ColComponent, RowComponent, CardImgDirective, CurrencyPipe],
  templateUrl: './SNAKE_CASE[tableName].component.html',
  styleUrl: './SNAKE_CASE[tableName].component.scss'
})
export class CAMEL_CASE_CAP[tableName]Component implements OnInit {

  isLoading = true;
  CAMEL_CASE[tableName]s: any[] = [];

  constructor(private router: Router, private CAMEL_CASE[tableName]Service: CAMEL_CASE_CAP[tableName]Service) {
    
  }

  //show(CAMEL_CASE[tableName]: any) {
  //  this.router.navigate(['/CAMEL_CASE[tableName]s/create/' + CAMEL_CASE[tableName].id])
  //}

  ngOnInit(): void {
    this.CAMEL_CASE[tableName]Service.findAll().subscribe(
      (data: any) => {
        this.CAMEL_CASE[tableName]s = data
        this.isLoading = false
      },
      error => {
        console.log("error ->" + error);
      }
    )
  }
}
