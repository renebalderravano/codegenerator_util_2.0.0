import { Routes } from "@angular/router";
import { PASCAL_CASE[tableName]List } from "./list/SNAKE_CASE[tableName].list";
import { PASCAL_CASE[tableName]Form } from "./form/SNAKE_CASE[tableName].form";
import { PASCAL_CASE[tableName] } from "./SNAKE_CASE[tableName]";

export default [
	{ path: '', component: PASCAL_CASE[tableName]  },
    { path: 'list', component: PASCAL_CASE[tableName]List  },
    { path: 'form', component: PASCAL_CASE[tableName]Form  }
] as Routes;