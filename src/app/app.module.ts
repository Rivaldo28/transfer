import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AgendarTransferenciaComponent } from './components/agendar-transferencia/agendar-transferencia.component';
import { ExtratoComponent } from './components/extrato/extrato.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterModule, Routes } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './components/navbar/navbar.component';
import { DashboardTaxasComponent } from './components/dashboard-taxas/dashboard-taxas.component';
import { ChartsModule } from 'ng2-charts';
import { LoadingComponent } from './components/loading/loading.component';

const routes: Routes = [
  { path: '', redirectTo: '/transferencia', pathMatch: 'full' }, 
  { path: 'transferencia', component: AgendarTransferenciaComponent },
  { path: 'extrato', component: ExtratoComponent },
  { path: 'dashboard-taxas', component: DashboardTaxasComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    AgendarTransferenciaComponent,
    ExtratoComponent,
    NavbarComponent,
    DashboardTaxasComponent,
    LoadingComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxMaskModule.forRoot(),
    FontAwesomeModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ChartsModule     
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
