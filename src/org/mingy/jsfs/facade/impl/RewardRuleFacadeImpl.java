package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mingy.jsfs.facade.IRewardRuleFacade;
import org.mingy.jsfs.model.Caches;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.RewardRule;
import org.mingy.jsfs.model.orm.GoodsEntity;
import org.mingy.jsfs.model.orm.GoodsTypeEntity;
import org.mingy.jsfs.model.orm.PositionEntity;
import org.mingy.jsfs.model.orm.RewardRuleEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;

public class RewardRuleFacadeImpl implements IRewardRuleFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public List<RewardRule> getRules() {
		List<RewardRule> list = new ArrayList<RewardRule>();
		for (RewardRuleEntity entity : entityDao
				.loadAll(RewardRuleEntity.class)) {
			RewardRule rule = Caches.load(RewardRule.class, entity.getId());
			if (rule == null) {
				rule = new RewardRule();
				rule.setId(entity.getId());
				Caches.save(RewardRule.class, rule);
			}
			rule.setName(entity.getName());
			Set<Position> positions = new HashSet<Position>();
			for (PositionEntity pe : entity.getPositions()) {
				positions.add(EntityConverts.toPosition(pe));
			}
			rule.setPositions(positions);
			rule.setGoodsType(entity.getGoodsType() != null ? EntityConverts
					.toGoodsType(entity.getGoodsType()) : null);
			rule.setGoods(entity.getGoods() != null ? EntityConverts
					.toGoods(entity.getGoods()) : null);
			rule.setScript(entity.getScript());
			rule.setDescription(entity.getDescription());
			list.add(rule);
		}
		return list;
	}

	@Override
	public void saveRule(RewardRule rule) {
		RewardRuleEntity entity = null;
		if (rule.getId() != null) {
			entity = entityDao.load(RewardRuleEntity.class, rule.getId());
		}
		if (entity == null) {
			entity = new RewardRuleEntity();
		}
		entity.setId(rule.getId());
		entity.setName(rule.getName());
		Set<PositionEntity> positions = new HashSet<PositionEntity>();
		for (Position position : rule.getPositions()) {
			PositionEntity pe = entityDao.load(PositionEntity.class,
					position.getId());
			positions.add(pe);
		}
		entity.setPositions(positions);
		entity.setGoodsType(rule.getGoodsType() != null ? entityDao.load(
				GoodsTypeEntity.class, rule.getGoodsType().getId()) : null);
		entity.setGoods(rule.getGoods() != null ? entityDao.load(
				GoodsEntity.class, rule.getGoods().getId()) : null);
		entity.setScript(rule.getScript());
		entity.setDescription(rule.getDescription());
		entityDao.save(entity);
		rule.setId(entity.getId());
		Caches.save(RewardRule.class, rule);
	}

	@Override
	public void deleteRule(Long id) {
		entityDao.delete(RewardRuleEntity.class, id);
		Caches.remove(RewardRule.class, id);
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
