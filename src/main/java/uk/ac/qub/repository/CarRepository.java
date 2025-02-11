// CarRepository.java
package uk.ac.qub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.qub.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}