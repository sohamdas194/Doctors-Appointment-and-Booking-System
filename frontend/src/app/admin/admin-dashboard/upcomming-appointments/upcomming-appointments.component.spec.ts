import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpcommingAppointmentsComponent } from './upcomming-appointments.component';

describe('UpcommingAppointmentsComponent', () => {
  let component: UpcommingAppointmentsComponent;
  let fixture: ComponentFixture<UpcommingAppointmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpcommingAppointmentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpcommingAppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
