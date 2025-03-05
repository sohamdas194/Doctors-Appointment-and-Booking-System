export class ViewAppointmentsDto {
  appointmentId: number;
  firstName: string;
  lastName: string;
  gender:string;
  age:number;
  appointmentDate: string;
  slotTiming: number;
  doctorComment: string;
  patientComments: string;
  currentStatus:string;

  constructor (data:any)
  {
    this.appointmentId=data.appointmentId;
    this.firstName=data.firstName;
    this.lastName=data.lastName;
    this.gender=data.gender;
    this.age=data.age;
    this.appointmentDate=data.appointmentDate;
    this.slotTiming=data.slotTiming;
    this.doctorComment=data.doctorComment;
    this.patientComments=data.patientComments;
    this.currentStatus=data.currentStatus;

  }
}

