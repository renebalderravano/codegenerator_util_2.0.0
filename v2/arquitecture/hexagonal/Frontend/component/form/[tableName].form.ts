import { Component } from '@angular/core';
import { ButtonModule } from "primeng/button";
import { ToastModule } from "primeng/toast";
import { MenubarModule } from "primeng/menubar";
import { IconFieldModule } from "primeng/iconfield";
import { InputIconModule } from "primeng/inputicon";
import { InputTextModule } from 'primeng/inputtext';
import { Router} from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ImportsModule } from 'src/import';
import { PASCAL_CASE[tableName]Service } from '../../service/SNAKE_CASE[tableName].service';

@Component({
  selector: 'SNAKE_CASE[tableName].form',
  standalone:true,
  imports: [ImportsModule,ButtonModule, ToastModule, MenubarModule, IconFieldModule, InputIconModule, InputTextModule],
  templateUrl: './SNAKE_CASE[tableName].form.html',
  styleUrl: './SNAKE_CASE[tableName].form.scss'
})
export class PASCAL_CASE[tableName]Form {

	form: FormGroup<any>
  /**
   *
   */
  constructor(
  private CAMEL_CASE[tableName]Service: PASCAL_CASE[tableName]Service,
  private router: Router) {
		this.form = new FormGroup({
[formControls]
		});
		
		let data = history.state.data;
		if (data)
		  this.form.setValue(data)
  }


  goToPASCAL_CASE[tableName]() {
    this.router.navigate(['/SNAKE_CASE[tableName]/list'])
  }
  
  save(){
	this.CAMEL_CASE[tableName]Service.save(this.form.value).subscribe(
	(data: any) => {
      this.form.setValue(data);
      //@ts-ignore
      //this.CAMEL_CASE[tableName]s.forEach((data) => (data.date = new Date(customer.date)));
    },
	(error: string)=>{
		console.log("error: "+ error);
	});
	}

}
