import { Component } from '@angular/core';

@Component({
    standalone: true,
    selector: 'app-footer',
    template: `<div class="layout-footer">
  Powered by
    <a href="https://softnet.com.mx/" target="_blank">
      <span>Softnet</span>
    </a>
    </div>`
})
export class AppFooter { }
