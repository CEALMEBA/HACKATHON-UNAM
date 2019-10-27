import { Injectable } from "@angular/core";
import * as firebase from "firebase/app";
import { Observable } from "rxjs";
import { User } from "../models/user";
import { AngularFireAuth } from "angularfire2/auth";
import { Router } from "@angular/router";
import { UserService } from "./user.service";

@Injectable()
export class AuthService {
  user: Observable<firebase.User>;
  userDetails: firebase.User = null;
  loggedUser;
  dbUser;
  constructor(
    private firebaseAuth: AngularFireAuth,
    private router: Router,
    private userService: UserService
  ) {
    this.user = firebaseAuth.authState;
    this.dbUser = new User();
    this.user.subscribe(user => {
     if (user) {
        this.userDetails = user;
       console.log(" email : " + this.userDetails.email);
        userService
          .isAdmin(this.userDetails.email)
          .snapshotChanges()
          .subscribe(data => {
            console.log(" email data : " + data);

            data.forEach(el => {

              const y = el.payload.doc.data();
              
              this.dbUser = y;
              this.dbUser.$key = el.payload.doc.id; 

              console.log("user : " + this.dbUser);

            });                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
          });   
      } else {
        this.userDetails = null;
      } 
    });
  }

  isLoggedIn(): boolean {
    if (this.userDetails !== null) {
      return true;
    }
  }

  logout() {
    this.loggedUser = null;
    this.firebaseAuth.auth.signOut().then(res => this.router.navigate(["/"]));
  }

  createUserWithEmailAndPassword(emailID: string, password: string) {
    return this.firebaseAuth.auth.createUserWithEmailAndPassword(
      emailID,
      password
    );
  }

  getLoggedInUser(): User {
    const loggedUser: User = new User();
    const user = this.firebaseAuth.auth.currentUser;

    if (user) {
      this.userDetails = user;
      if (user != null) {
        loggedUser.$key = this.dbUser["$key"];//user.uid;
        loggedUser.userName = this.dbUser["name"]; //user.displayName;
        loggedUser.emailId = user.email;
        loggedUser.phoneNumber = user.phoneNumber;
        loggedUser.avatar = user.photoURL;
        loggedUser.isAdmin = this.dbUser["isAdmin"];

        loggedUser.rfc = this.dbUser["rfc"];
        loggedUser.address = this.dbUser["address"];
        loggedUser.col = this.dbUser["col"];
        loggedUser.city = this.dbUser["city"];
        loggedUser.state = this.dbUser["state"];
        loggedUser.country = this.dbUser["country"];
        loggedUser.postalcode = this.dbUser["postalcode"];
        loggedUser.serie = this.dbUser["serie"];
        loggedUser.folio = this.dbUser["folio"];
        loggedUser.regimenfiscal = this.dbUser["regimenfiscal"];
        loggedUser.cuentabanco = this.dbUser["cuentabanco"];

        loggedUser.template = this.dbUser["template"];

      }
    } else {
      this.userDetails = null;
    }

    return loggedUser;
  }

  isAdmin(): boolean {
    const user = this.getLoggedInUser();
    // console.log("loggedUSer", user)
    // *ngIf="authService.isAdmin()"
    if (user != null) {
      if (user.isAdmin === true) {
        return true;
      }
    }
  }

  signInRegular(email, password) {
    const credential = firebase.auth.EmailAuthProvider.credential(
      email,
      password
    );
    return this.firebaseAuth.auth.signInWithEmailAndPassword(email, password);
  }

  signInWithGoogle() {
    return this.firebaseAuth.auth.signInWithPopup(
      new firebase.auth.GoogleAuthProvider()
    );
  }
}
