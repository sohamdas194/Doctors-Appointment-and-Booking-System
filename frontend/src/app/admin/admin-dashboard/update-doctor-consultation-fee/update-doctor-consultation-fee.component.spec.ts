import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDoctorConsultationFeeComponent } from './update-doctor-consultation-fee.component';

describe('UpdateDoctorConsultationFeeComponent', () => {
  let component: UpdateDoctorConsultationFeeComponent;
  let fixture: ComponentFixture<UpdateDoctorConsultationFeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateDoctorConsultationFeeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDoctorConsultationFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
