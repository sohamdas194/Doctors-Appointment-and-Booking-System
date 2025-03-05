export class AuthenticatedUser {
    constructor(
        public userId: bigint,
        public userEmail: string,
        public userRole: string,
        public authToken: string
    ) {}
}
