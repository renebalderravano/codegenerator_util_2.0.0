import { ChangeDetectionStrategy, Component, Input, OnChanges, Signal, SimpleChanges } from '@angular/core';
import { Router} from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ImportsModule } from 'src/import';
import { MessageService } from 'primeng/api';
import { PASCAL_CASE[tableName]Service } from '../../service/SNAKE_CASE[tableName].service';
//importService

@Component({
  selector: 'app-KEBAB_CASE[tableName]-form',
  standalone:true,
  imports: [ImportsModule],
  templateUrl: './SNAKE_CASE[tableName].form.html',
  styleUrl: './SNAKE_CASE[tableName].form.scss',
  providers: [MessageService],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PASCAL_CASE[tableName]Form implements OnChanges {

	@Input({ transform: (value: any) => value }) data!: Signal<any>;
	form: FormGroup<any>
//declarationsOption
  /**
   *
   */
  constructor(
//declarationsService
  private messageService: MessageService,
  private CAMEL_CASE[tableName]Service: PASCAL_CASE[tableName]Service,
  private router: Router) {
		this.form = new FormGroup({
[formControls]
		});

//executionsService
		let data = history.state.data;
		if (data)
		  this.form.setValue(data)
  }

	ngOnChanges(changes: SimpleChanges) {
		if (changes['data'] && changes['data'].currentValue !== undefined) {
			this.form.setValue( changes['data'].currentValue );
		}
	}

	goToPASCAL_CASE[tableName]() {
		this.router.navigate(['/SNAKE_CASE[tableName]/list'])
	}
  
	save(){
		this.CAMEL_CASE[tableName]Service.save(this.form.value).subscribe(
		(data: any) => {
		  this.form.setValue(data);
		  
		  this.messageService.add({ severity: 'success', summary: 'Guardado exitosamente.', detail: 'Guardado con id#'+data.id });
		  //@ts-ignore
		  //this.CAMEL_CASE[tableName]s.forEach((data) => (data.date = new Date(customer.date)));
		},
		(error: string)=>{
			console.log("error: "+ error);
		});
	}
	
	clear() {
		this.form.reset()
	}
	
	uploadFile(event: any) {
		const archivo: File = event.files[0];

		this.CAMEL_CASE[tableName]Service.upload(archivo).subscribe(
			(data: any) => {
				console.log('Archivo enviado');
			},
			error => {
				console.log(error);
				
			});
	}

}
