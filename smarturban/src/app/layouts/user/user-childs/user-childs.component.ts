
import { Component, OnInit } from '@angular/core';
import { Employee } from '../../../shared/models/employee';
import { AuthService } from '../../../shared/services/auth.service';
import { EmployeeService } from '../../../shared/services/employee.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';
  
@Component({
  selector: 'app-user-childs',
  templateUrl: './user-childs.component.html',
  styleUrls: ['./user-childs.component.scss']
})
export class UserChildsComponent implements OnInit {
  
      employeeList: Employee[];
      employeeObject: Employee;
    
      loading = false;
      brands = ['All', 'A', 'B', 'C', 'D', 'E'];
    
      selectedBrand: 'All';
    
      page = 1;
      constructor(
        public authService: AuthService,
        private employeeService: EmployeeService,
        private toastrService: ToastrService
      ) { }
    
      ngOnInit() {
        this.getAllEmployees();
      }
    
      getAllEmployees() {
        // this.spinnerService.show();
        console.log("getting employees");
  
        this.loading = true;
        const x = this.employeeService.getEmployees();
        x.snapshotChanges().subscribe(
          (employee) => {
            this.loading = false;
            // this.spinnerService.hide();
            this.employeeList = [];
            console.log(" employees" + employee);
  
            employee.forEach((element) => {
              //con y = element.payload.doc.data(). ..toJSON();
              //y['$key'] = element.key;
              this.employeeObject = element.payload.doc.data();
              this.employeeObject.$key = element.payload.doc.id; 
              console.log("data : " + this.employeeObject.$key); 
              this.employeeList.push(this.employeeObject as Employee);
            });
          },
          (err) => {
            this.toastrService.error('Error while fetching Employees', err);
          }
        );
      }
    
      removeEmployee(key: string) {
        this.employeeService.deleteEmployee(key);
      }
    }
    
 