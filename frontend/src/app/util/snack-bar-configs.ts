import { MatSnackBarConfig, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from "@angular/material/snack-bar";

let horizontalPosition: MatSnackBarHorizontalPosition = 'center';
let verticalPosition: MatSnackBarVerticalPosition = 'bottom';

export const CONFIG_SUCCESS: MatSnackBarConfig = {
    duration: 2 * 1000,
    horizontalPosition: horizontalPosition,
    verticalPosition: verticalPosition,
    panelClass: ['snack-bar-success']
};

export const CONFIG_ERROR: MatSnackBarConfig = {
    duration: 2 * 1000,
    horizontalPosition: horizontalPosition,
    verticalPosition: verticalPosition,
    panelClass: ['snack-bar-error']
};