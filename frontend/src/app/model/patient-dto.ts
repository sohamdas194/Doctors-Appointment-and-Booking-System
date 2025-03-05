export class PatientDto {
  email!: string;
  password!: string;
  role!: string;
  firstName!: string;
  lastName!: string;
  dob!: Date;
  height!: number;
  weight!: number;
  gender!: string;
  bloodGroup!: string;
  maritalStatus!: string;
  phone!: number;
  streetAddressLine1!: string;
  streetAddressLine2!: string;
  city!: string;
  state!: string;
  pin!: string;
  emergencyFirstName!: string;
  emergencyLastName!: string;
  emergencyRelationship!: string;
  emergencyContact!: string;
  reasonForRegistration!: string;
  additionalNotes!: string;
  insuranceCompany!:string;
  insuranceId!: string;
  policyHolderName!: string;
  policyDate!: Date;

  constructor (data: any) {
    this.email = data.email,
    this.password = data.password,
    this.role = 'patient',
    this.firstName = data.firstname,
    this.lastName = data.lastname,
    this.dob = data.dob,
    this.height= data.height,
    this.weight=data.weight,
    this.gender = data.gender,
    this.bloodGroup = data.bloodGroup,
    this.maritalStatus = data.maritalStatus,
    this.phone = data.contactNumber,
    this.streetAddressLine1 = data.street1,
    this.streetAddressLine2 = data.landmark
    this.city = data.city,
    this.state = data.state,
    this.pin = data.pincode,
    this.emergencyFirstName = data.emergencyFirstName,
    this.emergencyLastName = data.emergencyLastName,
    this.emergencyRelationship = data.emergencyRelationship,
    this.reasonForRegistration = data.reasonForRegistration,
    this.additionalNotes = data.additionalNotes,
    this.emergencyContact = data.emergencyContact,
    this.insuranceCompany = data.insuranceCompany,
    this.insuranceId = data.insuranceId,
    this.policyHolderName = data.policyHolderName,
    this.policyDate = data.policyDate
  }

}
































































