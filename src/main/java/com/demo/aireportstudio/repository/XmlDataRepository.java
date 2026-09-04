package com.demo.aireportstudio.repository;

import com.demo.aireportstudio.model.XmlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XmlDataRepository extends JpaRepository<XmlData, String> {

    Optional<XmlData> findByTitle(String title);
    boolean existsByTitle(String title);

    List<XmlData> findAllByIsDeletedFalse();
    Optional<XmlData> findByXmlDataIdAndIsDeletedFalse(String id);
    Optional<XmlData> findByTitleAndIsDeletedFalse(String title);
    boolean existsByTitleAndIsDeletedFalse(String title);
}
