import { Component, OnInit } from '@angular/core';
import { Transfer } from 'src/app/model/transfer.model';
import { ExportCSVService } from 'src/app/services/export-csv.service';
import { TransferenciaService } from 'src/app/services/transferencia.service';

@Component({
  selector: 'app-extrato',
  templateUrl: './extrato.component.html',
  styleUrls: ['./extrato.component.css']
})
export class ExtratoComponent implements OnInit {
  transferencias: Transfer[] = [];
  totalPages: number = 0;  
  currentPage: number = 0;  
  itemsPerPage: number = 6; 
  loading = false;

  public listExportCSV = new Array<Transfer>();
  public taxaLista: Array<Transfer> = [];

  constructor(private transferenciaService: TransferenciaService,
    private exportCsvService: ExportCSVService,
  ) { }

  ngOnInit(): void {
    this.carregarTransferencias();
    this.loading = true;
        setTimeout(() => {
          this.loading = false;
        }, 500);
  }
  
  public carregarTransferencias(page: number = 0, size: number = 6): void {
    this.transferenciaService.obterExtrato(page, size).subscribe({
      next: (data) => {
        this.transferencias = data.content; 
        this.totalPages = data.totalPages;  
        this.currentPage = data.number;       
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  public mudarPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.carregarTransferencias(page, this.itemsPerPage);
    }
  }

  public exportToCsv() {
    const header = ['Conta Origem', 'Conta Destino', 'Valor', 'Taxa',  'Aviso', 'Data da Transferência', 'Data de Agendamento'];
    const dados = this.transferencias.map(t => [
        t.accountOrigin || '',
        t.accountDestination || '',
        this.formatCurrency(t.transferValue) || '', 
        this.formatCurrency(t.rate) || '',
        t.notice || '', 
        this.formatDate(t.dateTransfer || '') || '', 
        this.formatDate(t.dateScheduling || '') || '' 
    ]);

    console.log(dados);

    this.exportCsvService.exportCsv(header, dados, 'transferencias.csv');
  }

  public formatDate(dateString: string): string {
      if (!dateString) return ''; 
      const date = new Date(dateString);
      const options: Intl.DateTimeFormatOptions = { day: '2-digit', month: '2-digit', year: 'numeric' };
      return date.toLocaleDateString('pt-BR', options);
  }

  public formatCurrency(value: number): string {
      return value !== null && value !== undefined ? value.toFixed(2).replace('.', ',') : '0,00';
  }
  
}
