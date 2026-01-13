import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ImportsModule } from 'src/import';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { CommonComponent } from '@/core/services/common.component';
import { PASCAL_CASE[tableName]Service } from '../../../../service/SCHEMA_NAME/SNAKE_CASE[tableName].service';

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
  
  visible: boolean = false;
  status: string | number = 'Pendiente';



  /**
   *
   */
  constructor(
	private CAMEL_CASE[tableName]Service: PASCAL_CASE[tableName]Service,
	public commonComponent: CommonComponent,
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
  
  download() {
    this.CAMEL_CASE[tableName]Service.download().subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'archivo.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
  
  uploadFile(event: any) {
    this.commonComponent.totalSizePercent = 70;
    const archivo: File = event.files[0];
    this.CAMEL_CASE[tableName]Service.upload(archivo).subscribe(
      (data: any) => {
        this.commonComponent.totalSizePercent = 100;
        this.status = 'Completado'
         this.messageService.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000 });
        setTimeout(() => {
         
        }, 500);
        console.log('Archivo enviado');
      },
      error => {
        console.log(error);
      });
  }
  
  showDialog() {
    this.visible = true;
  }

}

