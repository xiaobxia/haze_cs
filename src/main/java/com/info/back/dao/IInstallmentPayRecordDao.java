package com.info.back.dao;

import com.info.web.pojo.cspojo.InstallmentPayRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInstallmentPayRecordDao {

    public void insert(InstallmentPayRecord installmentPayRecord);

    public List<InstallmentPayRecord> findInstallmentList(String id);
}