package com.lv.Eshop.dao;

import com.lv.Eshop.beans.SequenceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceDao extends JpaRepository<SequenceInfo, String> {
    SequenceInfo findByName(String name);
}
