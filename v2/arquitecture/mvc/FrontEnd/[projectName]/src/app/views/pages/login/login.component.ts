import { Component, OnInit } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { IconDirective } from '@coreui/icons-angular';
import { ContainerComponent, RowComponent, ColComponent, CardGroupComponent, TextColorDirective, CardComponent, CardBodyComponent, FormDirective, InputGroupComponent, InputGroupTextDirective, FormControlDirective, ButtonDirective } from '@coreui/angular';
import { Router } from '@angular/router';
import { AppService } from '../../../app.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule, ContainerComponent, RowComponent, ColComponent, CardGroupComponent, TextColorDirective, CardComponent, CardBodyComponent, FormDirective, InputGroupComponent, InputGroupTextDirective, IconDirective, FormControlDirective, ButtonDirective, NgStyle]
})
export class LoginComponent implements OnInit {

  username: string = ''
  password: string = ''

  constructor(private router: Router, private appService: AppService) { }

  ngOnInit(): void {
    sessionStorage.removeItem('token')
  }

  login() {
    let res = this.appService.getToken(this.username, this.password)
    res.then(result => {
      sessionStorage.setItem('token', JSON.stringify(result.access_token))
      console.log(result);
      this.router.navigate(['/dashboard'])
    }).catch(error => {
      this.router.navigate(['/'])
    });
  }

  register() {
    this.router.navigate(['/register'])
  }
  
}
