package rs.kopanja.zcamtest.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.extId IN (:extIds)")
    List<User> findAllByExtId(@Param("extIds") List<Long> extIds);
}
