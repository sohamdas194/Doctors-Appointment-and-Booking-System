export class AppointmentDto {
    constructor(
        public userId: number,
        public memberId: number,
        public doctorAvailabilityId: number,
        public appointmentSlot: number,
        public patientComment: string
    ) {}
} 
