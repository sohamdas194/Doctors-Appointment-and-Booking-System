export class FamilyDto {
    memberId!: number;
    firstName!: string;
    lastName!: string;
    dob!: string;
    gender!: string;
    bloodGroup!: string;
    relationship!: string;
    contactNo!: string;
    height!: number;
    weight!: number;
    healthHistory!: string

    constructor (data: any) {
        this.memberId = data.memberId,
        this.firstName = data.firstname,
        this.lastName = data.lastname,
        this.dob = data.dob,
        this.gender = data.gender,
        this.bloodGroup = data.bloodGroup,
        this.contactNo =data.contactNo,
        this.relationship = data.relationship,
        this.height =data.height,
        this.weight = data.weight,
        this.healthHistory = data.healthHistory
        
    }
}
