package com.proyek.akhir.repository;

import com.proyek.akhir.domain.History;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the History entity.
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("select history from History history where history.user.login = ?#{authentication.name}")
    List<History> findByUserIsCurrentUser();

    default Optional<History> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<History> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<History> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select history from History history left join fetch history.user",
        countQuery = "select count(history) from History history"
    )
    Page<History> findAllWithToOneRelationships(Pageable pageable);

    @Query("select history from History history left join fetch history.user")
    List<History> findAllWithToOneRelationships();

    @Query("select history from History history left join fetch history.user where history.id =:id")
    Optional<History> findOneWithToOneRelationships(@Param("id") Long id);
}
