import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorAvailabilityStatusComponent } from './doctor-availability-status.component';

describe('DoctorAvailabilityStatusComponent', () => {
  let component: DoctorAvailabilityStatusComponent;
  let fixture: ComponentFixture<DoctorAvailabilityStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoctorAvailabilityStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorAvailabilityStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
