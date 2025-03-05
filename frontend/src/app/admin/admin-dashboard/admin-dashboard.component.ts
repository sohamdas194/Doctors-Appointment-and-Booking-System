import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject, Subscription, map, share, timer } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit, OnDestroy {

  userEmail!: string;
  curTime: Date = new Date();
  subscripton!: Subscription;

  appointmentsReset$: Subject<boolean>;
  updateFeesReset$: Subject<boolean>;

  constructor(
    public authService: AuthenticationService
  ) { 
    this.appointmentsReset$ = new Subject();
    this.updateFeesReset$ = new Subject();
  }

  ngOnInit(): void {
    this.userEmail = this.authService.getAuthenticateduserEmail() || '';
  
    this.subscripton = timer(0, 1000)
      .pipe(
        map(() => new Date()),
        share()
      )
      .subscribe(time => {
        this.curTime = time;
      });
  }

  ngOnDestroy(): void {
    if(this.subscripton) this.subscripton.unsubscribe();
  }

  resetAppointmentTabs() {
    this.appointmentsReset$.next(true);
  }

  resetFeeExpansion() {
    this.updateFeesReset$.next(true);
  }

}
