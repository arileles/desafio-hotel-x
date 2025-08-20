import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// PrimeNG
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { PaginatorModule } from 'primeng/paginator';
import { CardModule } from 'primeng/card';

// Seu componente principal
import { AppComponent } from './app.component';
import { HospedeComponent } from './hospede/hospede.component';

@NgModule({
  declarations: [AppComponent, HospedeComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    PaginatorModule,
    CardModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
