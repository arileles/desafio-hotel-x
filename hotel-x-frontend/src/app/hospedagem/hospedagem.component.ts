import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HospedagemService } from '../service/hospedagem.service';

@Component({
  selector: 'app-hospedagem',
  templateUrl: './hospedagem.component.html',
  styleUrls: ['./hospedagem.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class HospedagemComponent implements OnInit {
  hospedagens: any[] = [];

  novoCheckin: any = {
    hospede: '',
    dataEntrada: null,
    dataSaida: null,
    adicionalVeiculo: false,
    observacoes: '',
  };

  filtroHospedagem: string = 'ativos';

  paginaAtual: number = 0;
  tamanhoPagina: number = 3;

  constructor(private hospedagemService: HospedagemService) {}

  ngOnInit(): void {
    this.carregarHospedagens();
  }

  carregarHospedagens(): void {
    if (this.filtroHospedagem === 'ativos') {
      this.hospedagemService.getHospedesAtivos().subscribe({
        next: (data) => {
          this.hospedagens = data.content;
        },
        error: (err) => console.error('Erro ao carregar hóspedes ativos:', err),
      });
    } else {
      this.hospedagemService.getHospedesInativos().subscribe({
        next: (data) => {
          this.hospedagens = data.content;
        },
        error: (err) => console.error('Erro ao carregar hóspedes inativos:', err),
      });
    }
  }

  get hospedagensPaginadas() {
    const inicio = this.paginaAtual * this.tamanhoPagina;
    return this.hospedagens.slice(inicio, inicio + this.tamanhoPagina);
  }

  proximaPagina() {
    if ((this.paginaAtual + 1) * this.tamanhoPagina < this.hospedagens.length) {
      this.paginaAtual++;
    }
  }

  paginaAnterior() {
    if (this.paginaAtual > 0) {
      this.paginaAtual--;
    }
  }

  realizarCheckin(): void {
    if (!this.novoCheckin.hospede || !this.novoCheckin.dataEntrada) {
      alert('O CPF do hóspede e a data de entrada são obrigatórios para o check-in!');
      return;
    }

    // Cria um novo objeto para a requisição com as datas formatadas
    const checkinParaEnvio = {
      ...this.novoCheckin
    };

    if (this.novoCheckin.dataEntrada) {
      const data = new Date(this.novoCheckin.dataEntrada);
      const dia = String(data.getDate()).padStart(2, '0');
      const mes = String(data.getMonth() + 1).padStart(2, '0');
      const ano = data.getFullYear();
      const horas = String(data.getHours()).padStart(2, '0');
      const minutos = String(data.getMinutes()).padStart(2, '0');
      checkinParaEnvio.dataEntrada = `${dia}/${mes}/${ano} ${horas}:${minutos}`;
    }

    if (this.novoCheckin.dataSaida) {
      const data = new Date(this.novoCheckin.dataSaida);
      const dia = String(data.getDate()).padStart(2, '0');
      const mes = String(data.getMonth() + 1).padStart(2, '0');
      const ano = data.getFullYear();
      const horas = String(data.getHours()).padStart(2, '0');
      const minutos = String(data.getMinutes()).padStart(2, '0');
      checkinParaEnvio.dataSaida = `${dia}/${mes}/${ano} ${horas}:${minutos}`;
    }

    this.hospedagemService.createHospedagem(checkinParaEnvio).subscribe({
      next: (data) => {
        console.log('Check-in/Check-out realizado:', data);
        this.novoCheckin = {
          hospede: '',
          dataEntrada: null,
          dataSaida: null,
          adicionalVeiculo: false,
          observacoes: ''
        };
        this.carregarHospedagens();
      },
      error: (err) => alert('Erro ao realizar check-in/check-out: ' + err.error.message || err.message),
    });
  }
}
