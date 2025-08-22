import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HospedeService } from '../service/hospede.service';

@Component({
  selector: 'app-hospede',
  templateUrl: './hospede.component.html',
  styleUrls: ['./hospede.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class HospedeComponent implements OnInit {
  hospedes: any[] = [];
  hospedagens: any[] = [];

  novoHospede: any = {
    nome: '',
    cpf: '',
    telefone: '',
  };

  novoCheckin: any = {
    hospede: '',
    dataEntrada: null,
    dataSaida: null,
    adicionalVeiculo: false,
    observacoes: '',
  };

  filtroHospedagem: string = 'ativos';
  mostrarFormulario: boolean = false;

  constructor(private hospedeService: HospedeService) {}

  ngOnInit(): void {
    this.carregarHospedes();
  }

  carregarHospedes(): void {
    this.hospedeService.getHospedes().subscribe({
      next: (data) => {
        console.log('Resposta do backend:', data);
        this.hospedes = data.content;
      },
      error: (err) => console.error('Erro ao carregar hóspedes:', err),
    });
  }

  adicionarHospede(): void {
    if (
      !this.novoHospede.nome ||
      !this.novoHospede.cpf ||
      !this.novoHospede.telefone
    ) {
      alert('Nome, CPF e telefone são obrigatórios!');
      return;
    }

    this.hospedeService.addHospede(this.novoHospede).subscribe({
      next: (data) => {
        console.log('Hospede adicionado:', data);
        this.novoHospede = { nome: '', cpf: '', telefone: '' };
        this.carregarHospedes();
      },
      error: (err) => alert('Erro ao adicionar hóspede: ' + err.error.message || err.message),
    });
  }
}