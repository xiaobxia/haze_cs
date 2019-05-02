package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.CountCollectionAssessment;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountCollectionAssessmentDao {
	/**
	 * 查询所有的考核记录
	 * @param params
	 * @return
	 */
	public List<CountCollectionAssessment> findAll(HashMap<String, Object> params);
	public Integer findAllCount(HashMap<String, Object> params);
	/**
	 * 累计
	 * @param params
	 * @return
	 */
	public List<CountCollectionAssessment> findAllByGroup(HashMap<String, Object> params);
	public Integer findAllCountByGroup(HashMap<String, Object> params);
	
	/**
	 * 获取一条考核记录 
	 * @param id 要查询的记录id
	 * @return 查询到的记录对象
	 */
	public CountCollectionAssessment getOne(Integer id);
	/**
	 * 执行存储过程
	 * @param params
	 */
	public void callAssessment(HashMap<String, Object> params);
	public void callMGroupAssessment(HashMap<String, Object> params);

	/**
	 * 查询考核统计
	 */
	public List<CountCollectionAssessment> queryExamineList(HashMap<String, Object> params);

	/**
	 * 查询管理统计
     */
	public List<CountCollectionAssessment> queryManageList(HashMap<String, Object> params);

	/**
	 * 插入考核统计记录
	 * @param list
     */
	public void insertExamineList(List<CountCollectionAssessment> list);

	/**
	 * 插入管理统计记录
	 * @param list
     */
	public void insertManageList(List<CountCollectionAssessment> list);

	/**
	 * 删除管理统计记录
     */
	public void deleteManageList(HashMap<String, Object> params);
}
