import { Component, OnInit } from '@angular/core';
import { ChartOptions } from 'chart.js';
import { Transfer } from 'src/app/model/transfer.model';
import { TransferenciaService } from 'src/app/services/transferencia.service';


@Component({
  selector: 'app-dashboard-taxas',
  templateUrl: './dashboard-taxas.component.html',
  styleUrls: ['./dashboard-taxas.component.css']
})
export class DashboardTaxasComponent implements OnInit {
  transferencias: Transfer[] = [];
  loading = false;
  
  public chartOptions: ChartOptions = {
    responsive: true,
  };

  public chartLabels: string[] = [];
  public chartData: number[] = [];

  constructor(private transferenciaService: TransferenciaService) {}

  ngOnInit(): void {
    this.loadTransferencias();
    this.loading = true;
        setTimeout(() => {
          this.loading = false;
        }, 500);
  }

  public loadTransferencias(): void {
    this.transferenciaService.obterExtrato().subscribe(page => {
      this.transferencias = page.content;
      this.prepareChartData();
    });
  }

  public prepareChartData(): void {
    const taxaTotals: { [key: string]: number } = {};

    this.transferencias.forEach(transferencia => {
      if (!taxaTotals[transferencia.accountOrigin]) {
        taxaTotals[transferencia.accountOrigin] = 0;
      }
      taxaTotals[transferencia.accountOrigin] += transferencia.rate;
    });

    this.chartLabels = Object.keys(taxaTotals);
    this.chartData = Object.values(taxaTotals);
  }
}