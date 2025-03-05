import { TestBed } from '@angular/core/testing';

import { FamilyDetailsService } from './family-members.service';

describe('FamilyDetailsService', () => {
  let service: FamilyDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FamilyDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
