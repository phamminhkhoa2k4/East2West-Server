package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Homestay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface HomestayRepository extends JpaRepository<Homestay,Integer>{

    @Query(value = """
        WITH destination AS (
            SELECT ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geom
        )
        SELECT hs.homestayid, hs.title, hs.description, hs.photos,
               hs.wardid, hs.structureid, hs.type, hs.userid, hs.isapproved,
               hs.maxguest, hs.beds, hs.bathroom,
               hs.address, hs.longitude, hs.latitude,
               hs.cleaningfee, hs.instant, hs.room, hs.extrainfo, hs.geom
        FROM destination d
        INNER JOIN homestays hs ON ST_DWithin(ST_Transform(hs.geom, 4326), d.geom, :radius)
        INNER JOIN (
            SELECT h.homestayid, AVG(ha.pricepernight) AS pricepernight,
                   SUM(ha.pricepernight) AS total_amount
            FROM homestays h
            INNER JOIN homestayavailability ha ON h.homestayid = ha.homestayid
            WHERE ha.date BETWEEN :checkinDate AND :checkoutDate
              AND ha.status = :status
            GROUP BY h.homestayid
            HAVING COUNT(ha.date) = :nights
        ) AS vh ON hs.homestayid = vh.homestayid
        WHERE hs.maxguest >= :guests
        ORDER BY ST_Distance(ST_Transform(hs.geom, 4326), d.geom);
""", nativeQuery = true)
    List<Homestay> searchHomestay(@Param("longitude") Double longitude,
                                  @Param("latitude") Double latitude,
                                  @Param("radius") Double radius,
                                  @Param("checkinDate") LocalDate checkinDate,
                                  @Param("checkoutDate") LocalDate checkoutDate,
                                  @Param("nights") Integer nights,
                                  @Param("guests") Integer guests,
                                  @Param("status") String status);







    List<Homestay> findByUserid(int userid);

    List<Homestay> findByStructure_Structureid(int structureid);
}
