import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { UPI_PA, UPI_PN, UPI_TN } from 'src/app/app.constants';
import { TransactionDto } from 'src/app/model/transaction-dto';
import { TransactionService } from 'src/app/service/transaction.service';
import { CONFIG_ERROR, CONFIG_SUCCESS } from 'src/app/util/snack-bar-configs';

@Component({
  selector: 'app-payment-popup',
  templateUrl: './payment-popup.component.html',
  styleUrls: ['./payment-popup.component.css']
})
export class PaymentPopupComponent implements OnInit {

  public myAngularxQrCode!: string;
  appointmentId: number;
  paymentCompleted: boolean = false;

  transactionDets!: TransactionDto;

  constructor(
    @Inject(MatDialogRef<PaymentPopupComponent>) private dialogRef: any,
    @Inject(MAT_DIALOG_DATA) private data: any,
    private transactionService: TransactionService,
    private snackBar: MatSnackBar,
  ) {
    this.appointmentId = data.appointmentId;

    transactionService.createTransaction(this.appointmentId).subscribe(
      (response:any) => {
        this.transactionDets = response;
        console.log(this.transactionDets)
        this.myAngularxQrCode = `upi://pay?pa=${UPI_PA}&pn=${UPI_PN}&am=${this.transactionDets.consultationFee}&tn=${UPI_TN}`;
      }
    );
   }

  ngOnInit(): void {
   
  }

  onCancelClick(): void {
    this.dialogRef.close({paymentCompleted :this.paymentCompleted});
  }

  onPaidClick(): void {
    this.transactionService.closeTransaction(this.transactionDets.transactionId).subscribe(
      (response:any) => {
        this.openSnackBar("Updated!", CONFIG_SUCCESS);
        this.paymentCompleted = true;
        this.onCancelClick();
      }, error => {
        this.openSnackBar("Failed!!", CONFIG_ERROR);
        this.onCancelClick();
      }
    );
    
  }

  openSnackBar(message: string, config: MatSnackBarConfig) {
    this.snackBar.open(message, "close", config);
  }

}
