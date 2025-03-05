import { TestBed } from '@angular/core/testing';

import { DoctorAvailabilityServiceService } from './doctor-availability-service.service';

describe('DoctorAvailabilityServiceService', () => {
  let service: DoctorAvailabilityServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctorAvailabilityServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
