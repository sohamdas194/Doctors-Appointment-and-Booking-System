export class DoctorConsultationDto{
    appointmentId!:number;
    patientName!:string;
  //  familyFirstName!:string;
    gender!:string;
    age!:number;
    dateOfAppointment!:Date;
    appointmentSlot!:string;
    patientComments!:string;
    doctorComments!:string;
    appointmentStatus!:string;
    consultationFee!:number;
}