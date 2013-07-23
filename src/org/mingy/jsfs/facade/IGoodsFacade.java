package org.mingy.jsfs.facade;

import java.util.List;

import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;

public interface IGoodsFacade {

	List<GoodsType> getGoodsTypes();

	void saveGoodsType(GoodsType goodsType);

	void deleteGoodsType(Long id);

	List<Goods> getGoods();

	void saveGoods(Goods goods);

	void deleteGoods(Long id);
}
