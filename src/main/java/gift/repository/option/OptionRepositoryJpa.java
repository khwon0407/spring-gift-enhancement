package gift.repository.option;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepositoryJpa extends JpaRepository<Option, Long> {

}
