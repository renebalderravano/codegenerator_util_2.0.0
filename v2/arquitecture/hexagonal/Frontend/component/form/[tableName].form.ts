import { Component } from '@angular/core';
import { ButtonModule } from "primeng/button";
import { ToastModule } from "primeng/toast";
import { MenubarModule } from "primeng/menubar";
import { IconFieldModule } from "primeng/iconfield";
import { InputIconModule } from "primeng/inputicon";
import { InputTextModule } from 'primeng/inputtext';
import { Route, Router} from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { ImportsModule } from 'src/import';

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
  constructor(private router: Router) {
		this.form = new FormGroup({
[formControls]
		});
  }


  goToPASCAL_CASE[tableName]() {
    this.router.navigate(['/SNAKE_CASE[tableName]/list'])
  }

}
