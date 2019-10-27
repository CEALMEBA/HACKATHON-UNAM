import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { EmployeeService } from "../../../shared/services/employee.service";
import { Employee } from "../../../shared/models/employee";

declare var $: any;
declare var require: any;
declare var toastr: any;
const shortId = require("shortid");
const moment = require("moment");

@Component({
  selector: "app-add-employee",
  templateUrl: "./add-employee.component.html",
  styleUrls: ["./add-employee.component.scss"]
})
export class AddEmployeeComponent implements OnInit {

  selectedOption: string = "0";
  seeSelection: string = "";

	constructor(private employeeService: EmployeeService) {}

	employee: Employee = new Employee();


  id: number;
  id1: number;
  id2: number;
  id3: number;
  id4: number;
  id5: number;

  userRols: any[] = [
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
// TYPE OF CONTRACT
  userTips: any[] = [
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

  userRigs: any[] = [
    {id2: "01", rig: "Clase I"},
    {id2: "02", rig: "Clase II"},
    {id2: "03", rig: "Clase III"},
    {id2: "04", rig: "Clase IV"},
    {id2: "05", rig: "Clase V"},
    {id2: "06", rig: "No Aplica"},
  ];

  userJors: any[] = [
    {id3: "A1", jor: "A1"},
    {id3: "B1", jor: "B1"},
    {id3: "C1", jor: "C1"},
    {id3: "D1", jor: "D1"}
  ];

  userRecs: any[] = [
    {id4: "IP", rec: "Ingresos Propios."},
    {id4: "IF", rec: "Ingresos Federales."},
    {id4: "IM", rec: "Ingresos Mixtos."}
  ];

  userBcos: any[] = [
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

	ngOnInit() {}

	createEmployee(employeeForm: NgForm) {
		employeeForm.value["employeeId"] = "EMP_" + shortId.generate();
		employeeForm.value["employeeAdded"] = moment().unix();
		employeeForm.value["ratings"] = Math.floor(Math.random() * 5 + 1);
		if (employeeForm.value["employeeImageUrl"] === undefined) {
			employeeForm.value["employeeImageUrl"] = "http://via.placeholder.com/640x360/007bff/ffffff";
		}

		const date = employeeForm.value["employeeAdded"];

		this.employeeService.createEmployee(employeeForm.value);

		this.employee = new Employee();

    toastr.success("employee " + employeeForm.value[" employeeName "] + "is added successfully", "Employee Creation");

	}
}
