export interface FilteredAppointment {
    appointmentId: number;
    doctorName: string;
    doctorSpecialization: string;
    patientName: string;
    patientGender: string;
    patientAge: number;
    appointmentDate: Date;
    appointmentTimeSlot: string;
    appointmentStatus: string;
    patientComment: string;
    doctorComment: string;
}