package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mingy.jsfs.facade.IRewardRuleFacade;
import org.mingy.jsfs.facade.ISalaryFacade;
import org.mingy.jsfs.model.CalcSalaryCondition;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.RewardRule;
import org.mingy.jsfs.model.Salary;
import org.mingy.jsfs.model.Salary.StatDetail;
import org.mingy.jsfs.model.orm.SalesLogDetailEntity;
import org.mingy.jsfs.model.orm.SalesLogEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.ApplicationException;
import org.mingy.kernel.util.Calendars;

public class SalaryFacadeImpl implements ISalaryFacade {

	private static final Log logger = LogFactory.getLog(SalaryFacadeImpl.class);

	private IEntityDaoFacade entityDao;
	private IRewardRuleFacade rewardRuleFacade;

	@SuppressWarnings("unchecked")
	@Override
	public List<Salary> calculate(CalcSalaryCondition condition) {
		Map<Long, List<RewardRule>> map = new HashMap<Long, List<RewardRule>>();
		for (RewardRule rule : rewardRuleFacade.getRules()) {
			for (Position position : rule.getPositions()) {
				List<RewardRule> rules = map.get(position.getId());
				if (rules == null) {
					rules = new ArrayList<RewardRule>();
					map.put(position.getId(), rules);
				}
				rules.add(rule);
			}
		}
		List<SalesLogEntity> list = entityDao
				.query("SELECT e FROM SalesLogEntity e WHERE e.salesTime BETWEEN ?1 AND ?2 ORDER BY e.staff.position.id, e.staff.id",
						new Object[] {
								Calendars.getMinTimeOfMonth(condition
										.getMonth()),
								Calendars.getMaxTimeOfMonth(condition
										.getMonth()) }, false);
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		List<Salary> result = new ArrayList<Salary>();
		Salary salary = null;
		Map<RewardRule, StatDetail> details = new HashMap<RewardRule, StatDetail>();
		for (SalesLogEntity entity : list) {
			if (salary == null
					|| !salary.getStaff().getId()
							.equals(entity.getStaff().getId())) {
				if (salary != null) {
					salary.setAmount(calc(engine, details));
					result.add(salary);
				}
				salary = new Salary();
				salary.setStaff(EntityConverts.toStaff(entity.getStaff()));
				details.clear();
			}
			List<RewardRule> rules = map.get(entity.getStaff().getPosition()
					.getId());
			for (SalesLogDetailEntity detailEntity : entity.getDetails()) {
				salary.setSalesAmount(salary.getSalesAmount()
						+ detailEntity.getTotalPrice());
				if (rules != null && !rules.isEmpty()) {
					for (RewardRule rule : rules) {
						if ((rule.getGoods() != null && rule.getGoods().getId()
								.equals(detailEntity.getGoods().getId()))
								|| (rule.getGoodsType() != null && rule
										.getGoodsType()
										.getId()
										.equals(detailEntity.getGoods()
												.getType().getId()))) {
							StatDetail detail = details.get(rule);
							if (detail == null) {
								detail = new StatDetail();
								detail.setGoodsOrType(rule.getGoodsOrType());
								salary.getDetails().add(detail);
								details.put(rule, detail);
							}
							detail.setCount(detail.getCount()
									+ detailEntity.getCount());
							detail.setPrice(detail.getPrice()
									+ detailEntity.getTotalPrice());
							break;
						}
					}
				}
			}
		}
		if (salary != null) {
			salary.setAmount(calc(engine, details));
			result.add(salary);
		}
		return result;
	}

	private Double calc(ScriptEngine engine, Map<RewardRule, StatDetail> details) {
		Double amount = 0d;
		for (Entry<RewardRule, StatDetail> entry : details.entrySet()) {
			RewardRule rule = entry.getKey();
			StatDetail detail = entry.getValue();
			engine.put("count", detail.getCount());
			engine.put("price", detail.getPrice());
			try {
				detail.setAmount((Double) engine.eval("var fn = function() {"
						+ rule.getScript() + "};fn();"));
			} catch (ScriptException e) {
				if (logger.isErrorEnabled()) {
					logger.error("execute script error", e);
				}
				throw new ApplicationException("error.eval.script", e);
			}
			amount += detail.getAmount();
		}
		return amount;
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}

	public void setRewardRuleFacade(IRewardRuleFacade rewardRuleFacade) {
		this.rewardRuleFacade = rewardRuleFacade;
	}
}
