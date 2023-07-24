package net.therap.caffeinetesting.repository;

import net.therap.caffeinetesting.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author duity
 * @since 7/24/23
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
