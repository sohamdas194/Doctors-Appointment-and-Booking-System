export class ResetPasswordReq {
    constructor(
        public token: string,
        public newPassword: string,
    ) {}
}
