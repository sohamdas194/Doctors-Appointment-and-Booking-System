import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorProfileUpdateComponent } from './doctor-profile-update.component';

describe('DoctorProfileUpdateComponent', () => {
  let component: DoctorProfileUpdateComponent;
  let fixture: ComponentFixture<DoctorProfileUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoctorProfileUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorProfileUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
