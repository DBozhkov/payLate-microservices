class UserAccountModel {
    id?: number;
    oktaId: string;
    firstName: string;
    lastName: string;
    city?: string;
    zipCode?: string;
    nickName?: string;
    mobilePhone?: string;
    postalAddress?: string;

    constructor(
        oktaId: string,
        firstName: string,
        lastName: string,
        city?: string,
        zipCode?: string,
        nickName?: string,
        mobilePhone?: string,
        postalAddress?: string,
        id?: number
    ) {
        this.oktaId = oktaId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.zipCode = zipCode;
        this.nickName = nickName;
        this.mobilePhone = mobilePhone;
        this.postalAddress = postalAddress;
        this.id = id;
    }
}

export default UserAccountModel;
