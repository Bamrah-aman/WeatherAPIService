package com.skiapi.weatherapiservice.repository;

import com.skiapi.weatherapicommon.Entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationsRepository extends JpaRepository<Locations, String> {

    @Query("select l from Locations l where l.trashed = false ")
    public List<Locations> findUntrashed();

    @Query("select l from Locations l where l.trashed = false and l.code= ?1")
    public Locations locationFindByCode(String code);

    @Modifying
    @Query("update Locations set trashed = true where code= ?1")
    public void trashByCode(String code);
}
