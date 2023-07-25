package net.therap.caffeinetesting.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import net.therap.caffeinetesting.db.Employee;
import net.therap.caffeinetesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class AppConfiguration {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .scheduler(Scheduler.systemScheduler())
                .removalListener((key, value, cause) -> {
                    ResponseEntity<Optional<Employee>> response = (ResponseEntity) value;

                    Employee evictedEmployee = response.getBody().get();
                    System.out.println("Evicted Employee: " + evictedEmployee.toString());
                    evictedEmployee.setStatus("Inactive");

                    Employee savedEmployee = employeeRepository.save(evictedEmployee);
                    System.out.println("Saved After Eviction: " + savedEmployee.toString());
                });
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
