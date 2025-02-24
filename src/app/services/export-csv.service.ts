import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ExportCSVService {
  
  private csvSeparator = ',';

  exportCsv(header: string[], dados: any[], filename: string) {
    const csv = this.toCsv(header, dados);
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');

    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  private toCsv(header: string[], dados: any[]) {
    let csv = '\ufeff';

    csv += header.map(d => this.csvTryQuote(d)).join(this.csvSeparator) + '\n';

    dados.forEach((d: any[]) => {
      csv += d.map((value: any) => this.csvTryQuote(value)).join(this.csvSeparator) + '\n';
    });

    return csv;
  }

  private csvTryQuote(data: any): string {
    if (data === null || data === undefined) {
      return ''; 
    }

    const value = String(data);

    if (value.includes(this.csvSeparator) || value.includes('"')) {
      return `"${value.replace(/"/g, '""')}"`;
    }

    return value;
  }
}