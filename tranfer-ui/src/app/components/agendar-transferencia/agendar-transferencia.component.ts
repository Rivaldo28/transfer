import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { TransferenciaService } from 'src/app/services/transferencia.service';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { Transfer } from 'src/app/model/transfer.model';


@Component({
  selector: 'app-agendar-transferencia',
  templateUrl: './agendar-transferencia.component.html',
  styleUrls: ['./agendar-transferencia.component.css']
})
export class AgendarTransferenciaComponent implements OnInit {
  transferenciaForm: FormGroup;
  loading = false;

  constructor(private fb: FormBuilder, 
    private transferenciaService: TransferenciaService,
    private toastr: ToastrService) {
    this.transferenciaForm = this.fb.group({
      accountOrigin: ['', Validators.required],
      accountDestination: ['', Validators.required],
      transferValue: [null, [Validators.required, Validators.min(0.01)]],
      rate: [null], 
      dateTransfer: ['',  Validators.required],
      dateScheduling: [{ value: new Date().toISOString().split('T')[0], disabled: true }, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loading = true;
      setTimeout(() => {
        this.loading = false;
      }, 500);
  }

  public agendarTransferencia() {
    if (this.transferenciaForm.valid) {
      const transferencia: Transfer = {
        accountOrigin: this.transferenciaForm.value.accountOrigin,
        accountDestination: this.transferenciaForm.value.accountDestination,
        transferValue: this.transferenciaForm.value.transferValue 
        ? parseFloat(this.transferenciaForm.value.transferValue.toString().replace(',', '.').replace(/[^0-9.-]+/g, "")) 
        : 0,
        rate: this.transferenciaForm.value.rate !== null ? this.transferenciaForm.value.rate : null,
        dateTransfer: this.transferenciaForm.get('dateTransfer')?.value || null,
        dateScheduling: new Date().toISOString().split('T')[0]
      };

      this.transferenciaService.agendarTransferencia(transferencia).subscribe(
         response => {
           this.toastr.success('Transferência feita com sucesso.', 'Sucesso!');
           this.limpar();
          },
           error => {
            const errorMessage =  error.error;
            Swal.fire({
              icon: "error",
              title: "Oops...",
              text: errorMessage,
              footer: '<a href="#">O dia não pode ser menor que hoje nem maior que 51 dias.</a>'
            });
          }
       );
     } else {
      this.toastr.error('Por favor, preencha todos os campos obrigatórios.', 'Erro!');
     }
  } 

  public limpar() {
    this.transferenciaForm.reset({
      accountOrigin: '',
      accountDestination: '',
      transferValue: null,
      dateTransfer: ''
    });
  }

  get isFormEmpty(): boolean {
    return !Object.values(this.transferenciaForm.value).some(value => value);
  }


}