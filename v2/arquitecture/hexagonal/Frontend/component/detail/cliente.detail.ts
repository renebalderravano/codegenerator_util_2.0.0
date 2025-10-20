import { Component, ElementRef, ViewChild } from '@angular/core';
import { TabsModule } from "primeng/tabs";
import { StatsWidget } from "@/pages/dashboard/components/statswidget";
import { MenubarModule } from "primeng/menubar";
import { IconFieldModule } from "primeng/iconfield";
import { InputIconModule } from "primeng/inputicon";
import { ToastModule } from "primeng/toast";
import { MenuItem, MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from "primeng/button";
import { TieredMenuModule } from "primeng/tieredmenu";
import { DialogModule } from "primeng/dialog";
import { CommonModule } from '@angular/common';
import { InputNumberModule } from "primeng/inputnumber";
import { FormsModule } from '@angular/forms';
import { FileUploadModule } from "primeng/fileupload";
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'cliente',
  standalone: true,
  imports: [CommonModule, FormsModule, TabsModule, MenubarModule, IconFieldModule, InputIconModule, ToastModule, InputTextModule, ButtonModule, TieredMenuModule, DialogModule, InputNumberModule, FileUploadModule],
  templateUrl: './cliente.detail.html',
  styleUrl: './cliente.detail.scss',
  providers: [MessageService]
})
export class ClienteDetail {

submitted: any;
hideDialog() {
throw new Error('Method not implemented.');
}
saveProduct() {
throw new Error('Method not implemented.');
}

    openNew() {
        this.product = {};
        this.submitted = false;
        this.productDialog = true;
    }


  itemsProfile: MenuItem[] | undefined = [
    {
      label: 'Mopper',
      icon: 'pi pi-file',
      command: () => {
        this.openNew()
      }
    },
    {
      label: 'Cuotas iniciales',
      icon: 'pi pi-file-edit',

    },
    {
      label: 'Saldos iniciales',
      icon: 'pi pi-search',
    }
  ];productDialog: any;
product: any;
statuses: any;

customerName: String = ''
  /**
   *
   */
  constructor(private messageService: MessageService,
    private router: Router, private route: ActivatedRoute
  ) {
this.route.queryParams.subscribe(params => {
   this.customerName=params['name']
  });


  }

  regresar() {
 this.router.navigate(['/cliente/list'])
}

  nestedMenuItems = [
    {
      label: 'Filtros',
      icon: 'pi pi-fw pi pi-align-justify',
      command: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'File created', life: 3000 });
      }
    }

  ];

  @ViewChild('filter') filter!: ElementRef;

   uploadedFiles: any[] = [];
      onUpload(event: any) {
        for (const file of event.files) {
            this.uploadedFiles.push(file);
        }

        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
    }
}
