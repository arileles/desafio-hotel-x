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
  novoHospede: any = {
    nome: '',
    cpf: '',
    telefone: '',
    valorTotalGasto: '',
    valorUltimaHospedagem: '',
  };

  constructor(private hospedeService: HospedeService) {}

  ngOnInit(): void {
    this.carregarHospedes();
  }

  carregarHospedes(): void {
    this.hospedeService.getHospedes().subscribe({
      next: (data) => {
        console.log('Resposta do backend:', data);
        // data.content é o array real de hóspedes
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

    if (!/^\d{10,11}$/.test(this.novoHospede.telefone)) {
      alert('O telefone deve conter 10 ou 11 dígitos numéricos');
      return;
    }

    if (!/^[A-Za-zÀ-ÖØ-öø-ÿ ]+$/.test(this.novoHospede.nome)) {
      alert('O nome deve conter apenas letras e espaços.');
      return;
    }

    this.hospedeService.addHospede(this.novoHospede).subscribe({
      next: (data) => {
        console.log('Hospede adicionado:', data);
        this.novoHospede = { nome: '' }; // limpa o formulário
        this.carregarHospedes(); // recarrega a lista
      },
      error: (err) => console.error('Erro ao adicionar hóspede:', err),
    });
  }
}
