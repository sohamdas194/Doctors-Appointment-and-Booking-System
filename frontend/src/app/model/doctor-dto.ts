export class DoctorDto {
    
    email!: string;
    password!: string;
    role!: string;
    firstName!: string;
    lastName!: string;
    dateOfBirth!: Date;
    gender!: string;
    city!: string;
    country!: string;
    address1!: string;
    address2!: string;
    state!: string;
    pinCode!: string;
    phoneNumber!: string;
    bloodGroup!: string;
    specialization!: string;
    consultingFrom!: Date;


    constructor(data: any) {
        this.email = data.email;
        this.password = data.password;
        this.role = 'doctor';
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.dateOfBirth = data.dateOfBirth;
        this.gender = data.gender;
        this.city = data.city;
        this.country = data.country;
        this.address1 = data.address1;
        this.address2 = data.address2;
        this.state = data.state;
        this.pinCode = data.pinCode;
        this.phoneNumber = data.phoneNumber;
        this.bloodGroup = data.bloodGroup;
        this.specialization = data.specialization;
        this.consultingFrom = data.consultingFrom;
       
    }
    
}