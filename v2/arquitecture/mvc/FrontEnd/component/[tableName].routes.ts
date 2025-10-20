import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    data: {
      title: 'CAMEL_CASE[tableName]s'
    },
    children: [
      {
        path: '',
        redirectTo: 'list',
        pathMatch: 'full'
      },
      {
        path: 'list',
        loadComponent: () => import('./SNAKE_CASE[tableName].component').then(m => m.CAMEL_CASE_CAP[tableName]Component),
        data: {
          title: ''
        }
      },
      // {
      //   path: 'create/:id',
      //   loadComponent: () => import('../customer-publication/customer-publication.component').then(m => m.CustomerPublicationComponent),
      //   data: {
      //     title: 'Cotizar'
      //   }
      // }
    ]
  }
];


