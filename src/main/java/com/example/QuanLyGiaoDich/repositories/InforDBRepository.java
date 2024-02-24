/*
 * package com.example.QuanLyGiaoDich.repositories;
 * 
 * import java.util.List;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.stereotype.Repository;
 * 
 * import com.example.QuanLyGiaoDich.dto.InforSgaDto;
 * 
 * @Repository public interface InforDBRepository extends
 * JpaRepository<InforSgaDto, Long> {
 * 
 * @Query(value = "{call sys.get_sga_info()}", nativeQuery = true)
 * List<Object[]> getSgaInfo(); }
 */