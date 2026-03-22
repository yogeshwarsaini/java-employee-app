package employee_app.controller;

import employee_app.model.Employee;
import employee_app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ========== UI Routes ==========

    @GetMapping({"/", "/employees"})
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index";
    }

    @GetMapping("/employees/new")
    public String newEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee-form";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                                RedirectAttributes redirectAttributes) {
        employeeService.createEmployee(employee);
        redirectAttributes.addFlashAttribute("message", "Employee save ho gaya!");
        return "redirect:/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("message", "Employee delete ho gaya!");
        return "redirect:/employees";
    }

    // ========== REST API Routes ==========

    @GetMapping("/api/employees")
    @ResponseBody
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/api/employees")
    @ResponseBody
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/api/employees/{id}")
    @ResponseBody
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                    @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/api/employees/{id}")
    @ResponseBody
    public void deleteEmployeeApi(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
