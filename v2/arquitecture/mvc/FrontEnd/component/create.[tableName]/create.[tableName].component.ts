import { CommonModule, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CAMEL_CASE_CAP[tableName]Service } from '../../../service/SNAKE_CASE[tableName].service';

@Component({
  selector: 'app-create.SNAKE_CASE[tableName]',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create.SNAKE_CASE[tableName].component.html',
  styleUrl: './create.SNAKE_CASE[tableName].component.scss'
})
export class CreateCAMEL_CASE_CAP[tableName]Component implements OnInit {
  
  public CAMEL_CASE[tableName]Form = new FormGroup({
    [form_controls]
    navigationId: new FormControl()
  });
  imageurl: any;
  
 constructor(private CAMEL_CASE[tableName]Service: CAMEL_CASE_CAP[tableName]Service,
             private location: Location) {
    let CAMEL_CASE[tableName]: any = this.location.getState();
    if(CAMEL_CASE[tableName].navigationId > 1){
      //this.CAMEL_CASE[tableName]Form.setValue(CAMEL_CASE[tableName])
      //this.imageurl = atob(this.CAMEL_CASE[tableName]Form.controls.image.value)
    }    
 }

 ngOnInit(): void {  
 }

 save(){
  this.CAMEL_CASE[tableName]Service.saveCAMEL_CASE_CAP[tableName](this.CAMEL_CASE[tableName]Form.value).subscribe(
    res=>{

      alert("Guardado")
    },
    error=>{
    }
  );
 }

 onFileSelected(event: Event): void {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    const reader = new FileReader();

    let typeFile = file.type
    reader.onload = () => {
      const arrayBuffer = reader.result as ArrayBuffer;
      let byteArray = new Uint8Array(arrayBuffer);
      // const STRING_CHAR = String.fromCharCode.apply(null, byteArray);
      const STRING_CHAR = byteArray.reduce((data, byte)=> {
        return data + String.fromCharCode(byte);
        }, '');
      let base64String = btoa(STRING_CHAR);
      this.imageurl = 'data:'+typeFile+';base64, ' + base64String;
      let base64 = btoa(this.imageurl);
      this.CAMEL_CASE[tableName]Form.controls.image.setValue(base64);
      console.log(byteArray);
    };
    reader.readAsArrayBuffer(file);
  }
}

}
