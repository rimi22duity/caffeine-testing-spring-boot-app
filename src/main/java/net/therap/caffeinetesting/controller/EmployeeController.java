package net.therap.caffeinetesting.controller;

import lombok.extern.slf4j.Slf4j;
import net.therap.caffeinetesting.customKey.CustomKeyGenerator;
import net.therap.caffeinetesting.db.Employee;
import net.therap.caffeinetesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author duity
 * @since 7/24/23
 */

@RestController
@RequestMapping("/employees")
@Slf4j
@CacheConfig(cacheNames = {"employees"})
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomKeyGenerator customKeyGenerator;

    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee) {
        employee.setStatus("Active");
        Employee savedEmployee = employeeRepository.save(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(keyGenerator = "customKeyGenerator")
    public ResponseEntity<Optional<Employee>> find(@PathVariable(value = "id") Integer id) {
        log.info("Employee Data Fetched From Database:: " + id);
        return ResponseEntity.ok(employeeRepository.findById(id));
    }

    @PutMapping("/{id}")
    @CachePut(key = "#id")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Integer id,
                                                   @RequestBody Employee employeeDetails) {
        Optional<Employee> employee = employeeRepository.findById(id);
        employee.get().setName(employeeDetails.getName());
        employee.get().setStatus("Active");
        final Employee updatedEmployee = employeeRepository.save(employee.get());
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id", allEntries = true)
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable(value = "id") Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.delete(employee.get());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}