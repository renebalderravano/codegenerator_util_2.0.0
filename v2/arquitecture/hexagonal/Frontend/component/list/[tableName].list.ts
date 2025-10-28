import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ImportsModule } from 'src/import';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { PASCAL_CASE[tableName]Service } from '../../service/SNAKE_CASE[tableName].service';

@Component({
  selector: 'app-KEBAB_CASE[tableName]-list',
  imports: [CommonModule,
    ImportsModule,
    FormsModule],
  providers: [
    MessageService
  ],
  standalone: true,
  templateUrl: './SNAKE_CASE[tableName].list.html',
  styleUrl: './SNAKE_CASE[tableName].list.scss'
})
export class PASCAL_CASE[tableName]List {

  loading: boolean = true;
  CAMEL_CASE[tableName]s: any[] = [];
  @ViewChild('filter') filter!: ElementRef;
  @Output() dataEmitter = new EventEmitter<any>();

  /**
   *
   */
  constructor(
	private CAMEL_CASE[tableName]Service: PASCAL_CASE[tableName]Service,
    private messageService: MessageService,
    private router: Router) {
	
    this.CAMEL_CASE[tableName]Service.findAll().subscribe(
	(data: []) => {
      this.CAMEL_CASE[tableName]s = data;
      this.loading = false;
	  
      // @ts-ignore
      //this.CAMEL_CASE[tableName]s.forEach((data) => (data.date = new Date(customer.date)))
    },
	(error: string)=>{
		console.log("error: "+ error);
	});
  }


  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = '';
  }

  goToPASCAL_CASE[tableName]() {
    this.router.navigate(['/SNAKE_CASE[tableName]/form'])
  }

  update(data: any) {
   // this.router.navigate(['/SNAKE_CASE[tableName]/form'],{ state: { data: data } })
     this.dataEmitter.emit(data)
  }

}

