import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AgendarTransferenciaComponent } from './agendar-transferencia.component';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { TransferenciaService } from 'src/app/services/transferencia.service';
import { ToastrService } from 'ngx-toastr';
import { of, throwError } from 'rxjs';
import Swal from 'sweetalert2';
import { By } from '@angular/platform-browser';

describe('AgendarTransferenciaComponent', () => {
  let component: AgendarTransferenciaComponent;
  let fixture: ComponentFixture<AgendarTransferenciaComponent>;
  let transferenciaService: jasmine.SpyObj<TransferenciaService>;
  let toastrService: jasmine.SpyObj<ToastrService>;
  let swalSpy: jasmine.Spy; 

  beforeEach(async () => {
    const transferenciaServiceMock = jasmine.createSpyObj('TransferenciaService', ['agendarTransferencia']);
    const toastrServiceMock = jasmine.createSpyObj('ToastrService', ['success', 'error']);

    await TestBed.configureTestingModule({
      declarations: [ AgendarTransferenciaComponent ],
      imports: [ ReactiveFormsModule ],
      providers: [
        FormBuilder,
        { provide: TransferenciaService, useValue: transferenciaServiceMock },
        { provide: ToastrService, useValue: toastrServiceMock }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgendarTransferenciaComponent);
    component = fixture.componentInstance;
    transferenciaService = TestBed.inject(TransferenciaService) as jasmine.SpyObj<TransferenciaService>;
    toastrService = TestBed.inject(ToastrService) as jasmine.SpyObj<ToastrService>;
    fixture.detectChanges();

    swalSpy = spyOn(Swal, 'fire'); 
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    expect(component.transferenciaForm).toBeTruthy();
    expect(component.transferenciaForm.get('accountOrigin')).toBeTruthy();
    expect(component.transferenciaForm.get('accountDestination')).toBeTruthy();
    expect(component.transferenciaForm.get('transferValue')).toBeTruthy();
  });

  it('should call agendarTransferencia and show success toast on success', () => {
    const transferencia = {
      accountOrigin: '1234',
      accountDestination: '5678',
      transferValue: 100.50,
      rate: 2.0,
      dateTransfer: '2025-02-25',
      dateScheduling: '2025-02-23'
    };
    component.transferenciaForm.setValue({
      accountOrigin: '1234',
      accountDestination: '5678',
      transferValue: 100.50,
      rate: 2.0,
      dateTransfer: '2025-02-25',
      dateScheduling: '2025-02-23'
    });

    transferenciaService.agendarTransferencia.and.returnValue(of(transferencia));

    component.agendarTransferencia();

    expect(toastrService.success).toHaveBeenCalledWith('Transferência feita com sucesso.', 'Sucesso!');
  });

  it('should show error toast if form is invalid', () => {
    component.transferenciaForm.setValue({
      accountOrigin: '',
      accountDestination: '',
      transferValue: '',
      rate: '',
      dateTransfer: '',
      dateScheduling: ''
    });

    component.agendarTransferencia();

    expect(toastrService.error).toHaveBeenCalledWith('Por favor, preencha todos os campos obrigatórios.', 'Erro!');
  });

  it('should show Swal error on service failure', () => {
    const errorResponse = { error: 'Error message' };
    
    component.transferenciaForm.setValue({
      accountOrigin: '1234',
      accountDestination: '5678',
      transferValue: 100.50,
      rate: 2.0,
      dateTransfer: '2025-02-25',
      dateScheduling: '2025-02-23'
    });

    transferenciaService.agendarTransferencia.and.returnValue(throwError(errorResponse));

    component.agendarTransferencia();

    expect(swalSpy).toHaveBeenCalledWith({
      icon: "error",
      title: "Oops...",
      text: "Error message",
      footer: '<a href="#">O dia não pode ser menor que hoje nem maior que 51 dias.</a>',
    });
  });
});
