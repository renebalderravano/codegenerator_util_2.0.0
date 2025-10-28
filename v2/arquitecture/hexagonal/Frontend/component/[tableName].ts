import { Component } from '@angular/core';
import { PASCAL_CASE[tableName]List } from './list/SNAKE_CASE[tableName].list';
import { PASCAL_CASE[tableName]Form } from "./form/SNAKE_CASE[tableName].form";

@Component({
  selector: 'app-KEBAB_CASE[tableName]',
  imports: [PASCAL_CASE[tableName]List, PASCAL_CASE[tableName]Form],
  templateUrl: './SNAKE_CASE[tableName].html'
})
export class PASCAL_CASE[tableName]{

	$data: any;

  /**
   *
   */
  constructor(
  ) {
    
  }
  retriveData(data: any){
    this.$data = data;
  }

}
