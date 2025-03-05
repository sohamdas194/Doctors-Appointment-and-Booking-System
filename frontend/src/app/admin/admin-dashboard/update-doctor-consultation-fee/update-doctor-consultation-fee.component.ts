import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { AdminService } from 'src/app/service/admin.service';
import { CONFIG_ERROR, CONFIG_SUCCESS } from 'src/app/util/snack-bar-configs';

@Component({
  selector: 'app-update-doctor-consultation-fee',
  templateUrl: './update-doctor-consultation-fee.component.html',
  styleUrls: ['./update-doctor-consultation-fee.component.css']
})
export class UpdateDoctorConsultationFeeComponent implements OnInit, AfterViewInit {

  searchDataForm!: FormGroup;

  newConsultationFees: {[key: number]: number} = {};

  displayedColumns: string[] = ['name', 'specialization', 'experience', 'fee', 'action'];
  dataSource: any = new MatTableDataSource();

  @Input() resetToDefaultValues$!: Observable<any>;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar,
  ) { 
    this.searchDataForm = this.formBuilder.group({
      name: ['']
    });

  }

  ngOnInit(): void {
    this.onShowAll();

    this.resetToDefaultValues$.subscribe(value => {
      if(value === true) this.onShowAll();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  onShowAll(): void {
    this.onResetClick(true);

    this.adminService.searchDoctors(this.searchDataForm.value).subscribe(
      response => {
        this.dataSource.data = response;
      }
    );
  }

  onResetClick(clearTableFlag: boolean): void {
    let searchDataValues = this.searchDataForm.value;
    searchDataValues.name='';
    this.searchDataForm.reset(searchDataValues);

    if(clearTableFlag) this.dataSource.data = [];
    else this.onShowAll();
  }

  onSearchClick(): void {
    this.adminService.searchDoctors(this.searchDataForm.value).subscribe(
      response => {
        this.dataSource.data = response;
      }
    );
  }

  onUpdateClick(doctorId: number): void {
    this.adminService.updateConsultationFee(doctorId, this.newConsultationFees[doctorId]).subscribe(
      response => {
        this.openSnackBar("Updated", CONFIG_SUCCESS);
      }, error => {
        this.openSnackBar("Update Failed!", CONFIG_ERROR);
      }
    );
  }

  openSnackBar(message: string, config: MatSnackBarConfig) {
    this.snackBar.open(message, "close", config);
  }

  addConsultationFee(event: any, doctorId: any) {
    this.newConsultationFees[doctorId] = event.target.value;
  }

}
