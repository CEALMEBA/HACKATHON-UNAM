
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Employee } from "../../../shared/models/employee";
import { ActivatedRoute } from "@angular/router";
import { NgForm } from "@angular/forms";
import { EmployeeService } from "../../../shared/services/employee.service";
import { ToastrService } from "src/app/shared/services/toastr.service";
import { AddEmployeeComponent } from "../add-employee/add-employee.component";



@Component({
  selector: "app-employee-detail",
  templateUrl: "./employee-detail.component.html",
  styleUrls: ["./employee-detail.component.scss"]
})
export class EmployeeDetailComponent implements OnInit {
 selectedOption = "0";
 seeSelection = "";

  private sub: any;
employee: Employee;

  banks;
  private regime: ({ id: string; rol: string })[];
  private contracts: ({ id1: string; tip: string })[];
  private ingresos: ({ rec: string; id4: string })[];
  private journeys: ({ id3: string; jor: string })[];
  private risks: ({ rig: string; id2: string })[];

constructor(
  private route: ActivatedRoute,
  private employeeService: EmployeeService,
  private toastrService: ToastrService
) {
  this.employee = new Employee();
    this.banks = [
      {id5: "01", bco: "BANAMEX"},
      {id5: "02", bco: "BBVA"},
      {id5: "03", bco: "SANTANDER"},
      {id5: "04", bco: "HSBC"},
      {id5: "05", bco: "INBURSA"},
      {id5: "06", bco: "SCOTIABANK"},
      {id5: "07", bco: "BANREGIO"},
      {id5: "08", bco: "AFIRME"},
      {id5: "09", bco: "BANORTE"},
      {id5: "10", bco: "AMERICAN EXPRESS"}
    ];
    this.regime = [
      {id: "02", rol: "Sueldos"},
      {id: "03", rol: "Jubilados"},
      {id: "04", rol: "Pensionados"},
      {id: "05", rol: "Asimilados Miembros Sociedades Cooperativas Produccion"},
      {id: "06", rol: "Asimilados Integrantes Sociedades Asociaciones Civiles"},
      {id: "07", rol: "Asimilados Miembros consejos"},
      {id: "08", rol: "Asimilados comisionistas"},
      {id: "09", rol: "Asimilados Honorarios"},
      {id: "10", rol: "Asimilados acciones"},
      {id: "13", rol: "Indeminizacion o Separacion"},
      {id: "99", rol: "Otro Regimen"}
    ];
    this.contracts = [
      {id1: "01", tip: "Contrato de trabajo por tiempo indeterminado."},
      {id1: "02", tip: "Contrato de trabajo para obra determinada."},
      {id1: "03", tip: "Contrato de trabajo por tiempo determinado."},
      {id1: "04", tip: "Contrato de trabajo por temporada."},
      {id1: "05", tip: "Contrato de trabajo sujeto a prueba."},
      {id1: "06", tip: "Modalidad de contratación por pago de hora laborada."},
      {id1: "07", tip: "Contrato de trabajo con capacitación inicial."},
      {id1: "08", tip: "Modalidad de trabajo por comisión laboral."},
      {id1: "09", tip: "Jubilación, pensión, retiro."},
      {id1: "10", tip: "Otro contrato."}
    ];
    this.ingresos = [
      {id4: "IP", rec: "Ingresos Propios."},
      {id4: "IF", rec: "Ingresos Federales."},
      {id4: "IM", rec: "Ingresos Mixtos."}
    ];
    this.journeys = [
      {id3: "A1", jor: "A1"},
      {id3: "B1", jor: "B1"},
      {id3: "C1", jor: "C1"},
      {id3: "D1", jor: "D1"}
    ];
    this.risks = [
      {id2: "01", rig: "Clase I"},
      {id2: "02", rig: "Clase II"},
      {id2: "03", rig: "Clase III"},
      {id2: "04", rig: "Clase IV"},
      {id2: "05", rig: "Clase V"},
      {id2: "06", rig: "No Aplica"},
    ];
}

ngOnInit() {
  this.sub = this.route.params.subscribe((params) => {
const id = params["id"]; // (+) converts string 'id' to a number
    this.getEmployeeDetail(id);
});
}

getEmployeeDetail(id: string) {
  // this.spinnerService.show();
  const x = this.employeeService.getEmployeeById(id);
  console.log("id " + id);
  x.snapshotChanges().subscribe(
    (employee) => {
      console.log("employee" + employee)
      // this.spinnerService.hide();
      // const y = employee.payload.data() as employee;
      this.employee = employee.payload.data();

        this.employee['$key'] = id;
// y['$key'] = id;
// this.employee = y;
			},
			(error) => {
				this.toastrService.error("Error while fetching Employee Detail", error);
			}
		);
	}

  updateEmployee(mykey : string, employee: NgForm){


    console.log("Updating Employee" + mykey + " - " + employee);

    this.employeeService.updateEmployee(mykey, employee.value);

    this.toastrService.success("Employee was updated successfully", "Employee Updated");

  }

	addToCart(employee: Employee) {
		this.employeeService.addToCart(employee);
	}

	ngOnDestroy() {
		this.sub.unsubscribe();
	}

}
