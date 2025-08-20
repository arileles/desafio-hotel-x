import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HospedeComponent } from './hospede.component';

describe('HospedeComponent', () => {
  let component: HospedeComponent;
  let fixture: ComponentFixture<HospedeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HospedeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HospedeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
