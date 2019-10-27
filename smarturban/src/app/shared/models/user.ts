export class User {
  $key: string;
  userName: string;
  emailId: string;
  password: string;
  location: {
    lat: number;
    lon: number;
  };
  phoneNumber: string;
  createdOn: string;
  isAdmin: boolean;
  avatar: string;
  firstName: string;
  lastName: string;
  
  rfc: string;
  col: string;
  address: string;
  city: string;
  country: string;
  state: string;
  postalcode: number;
  serie: string;
  folio: string;  
  regimenfiscal : string;
  cuentabanco : string;

  template: string;
}

export class UserDetail {
  $key: string;
  firstName: string;
  lastName: string;
  userName: string;
  emailId: string;
  address1: string;
  address2: string;
  country: string;
  state: string;
  zip: number;
}
