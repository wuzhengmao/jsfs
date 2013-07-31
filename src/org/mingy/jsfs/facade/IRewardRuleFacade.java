package org.mingy.jsfs.facade;

import java.util.List;

import org.mingy.jsfs.model.RewardRule;

public interface IRewardRuleFacade {

	List<RewardRule> getRules();

	void saveRule(RewardRule rule);

	void deleteRule(Long id);
}
