import { TestBed } from '@angular/core/testing';

import { DoctorRegistrationService } from './doctor-registration.service';

describe('DoctorRegistrationService', () => {
  let service: DoctorRegistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctorRegistrationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
