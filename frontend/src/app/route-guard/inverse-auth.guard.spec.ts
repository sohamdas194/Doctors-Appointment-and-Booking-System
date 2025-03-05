import { TestBed } from '@angular/core/testing';

import { InverseAuthGuard } from './inverse-auth.guard';

describe('InverseAuthGuard', () => {
  let guard: InverseAuthGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(InverseAuthGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
