package org.mingy.jsfs.ui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;
import org.mingy.kernel.context.GlobalBeanContext;

public abstract class Catalogs {

	private static final IStaffFacade staffFacade = GlobalBeanContext
			.getInstance().getBean(IStaffFacade.class);
	private static final IGoodsFacade goodsFacade = GlobalBeanContext
			.getInstance().getBean(IGoodsFacade.class);

	private static Catalog root;
	private static Map<Object, Catalog> catalogs;
	@SuppressWarnings("unchecked")
	private static List<Position> positionList = new WritableList();
	@SuppressWarnings("unchecked")
	private static List<Staff> staffList = new WritableList();
	@SuppressWarnings("unchecked")
	private static List<GoodsType> goodsTypeList = new WritableList();
	@SuppressWarnings("unchecked")
	private static List<Goods> goodsList = new WritableList();
	private static boolean inited = false;

	public static boolean isInited() {
		return inited;
	}

	public static void loadAll() {
		root = new Catalog(Catalog.TYPE_ROOT);
		Catalog staffCatalog = new Catalog(Catalog.TYPE_STAFF);
		staffCatalog.setParent(root);
		root.getChildren().add(staffCatalog);
		Catalog goodsCatalog = new Catalog(Catalog.TYPE_GOODS);
		goodsCatalog.setParent(root);
		root.getChildren().add(goodsCatalog);
		Catalog ruleCatalog = new Catalog(Catalog.TYPE_RULE);
		ruleCatalog.setParent(root);
		root.getChildren().add(ruleCatalog);
		catalogs = new HashMap<Object, Catalog>();
		positionList.clear();
		staffList.clear();
		goodsTypeList.clear();
		goodsList.clear();
		loadStaffs(staffCatalog);
		loadGoods(goodsCatalog);
		inited = true;
	}

	private static void loadStaffs(Catalog parent) {
		for (Position position : staffFacade.getPositions()) {
			Catalog catalog = new Catalog(position);
			catalog.setParent(parent);
			parent.getChildren().add(catalog);
			catalogs.put(position, catalog);
			positionList.add(position);
		}
		for (Staff staff : staffFacade.getStaffs()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(staff.getPosition())) {
					Catalog child = new Catalog(staff);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(staff, child);
					staffList.add(staff);
					break;
				}
			}
		}
	}

	private static void loadGoods(Catalog parent) {
		for (GoodsType goodsType : goodsFacade.getGoodsTypes()) {
			Catalog catalog = new Catalog(goodsType);
			catalog.setParent(parent);
			parent.getChildren().add(catalog);
			catalogs.put(goodsType, catalog);
			goodsTypeList.add(goodsType);
		}
		for (Goods goods : goodsFacade.getGoods()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(goods.getType())) {
					Catalog child = new Catalog(goods);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(goods, child);
					goodsList.add(goods);
					break;
				}
			}
		}
	}

	public static void updateCatalog(Catalog catalog, Position position) {
		if (catalog.getValue() == null) {
			catalog.setValue(position);
			getCatalog(Catalog.TYPE_STAFF).getChildren().add(catalog);
			catalogs.put(position, catalog);
			positionList.add(position);
		} else if (position != catalog.getValue()) {
			position.copyTo((Position) catalog.getValue());
		}
	}

	public static void updateCatalog(Catalog catalog, Staff staff) {
		if (catalog.getValue() == null) {
			catalog.setValue(staff);
			for (Catalog c : getCatalog(Catalog.TYPE_STAFF).getChildren()) {
				if (c.getValue().equals(staff.getPosition())) {
					catalog.setParent(c);
					c.getChildren().add(catalog);
					catalogs.put(staff, catalog);
					staffList.add(staff);
					break;
				}
			}
		} else {
			if (staff != catalog.getValue()) {
				staff.copyTo((Staff) catalog.getValue());
				staff = (Staff) catalog.getValue();
			}
			if (!catalog.getParent().getValue().equals(staff.getPosition())) {
				catalog.getParent().getChildren().remove(catalog);
				for (Catalog c : Catalogs.getCatalog(Catalog.TYPE_STAFF)
						.getChildren()) {
					if (c.getValue().equals(staff.getPosition())) {
						catalog.setParent(c);
						c.getChildren().add(catalog);
						break;
					}
				}
			}
		}
	}

	public static void updateCatalog(Catalog catalog, GoodsType goodsType) {
		if (catalog.getValue() == null) {
			catalog.setValue(goodsType);
			getCatalog(Catalog.TYPE_GOODS).getChildren().add(catalog);
			catalogs.put(goodsType, catalog);
			goodsTypeList.add(goodsType);
		} else if (goodsType != catalog.getValue()) {
			goodsType.copyTo((GoodsType) catalog.getValue());
		}
	}

	public static void updateCatalog(Catalog catalog, Goods goods) {
		if (catalog.getValue() == null) {
			catalog.setValue(goods);
			for (Catalog c : getCatalog(Catalog.TYPE_GOODS).getChildren()) {
				if (c.getValue().equals(goods.getType())) {
					catalog.setParent(c);
					c.getChildren().add(catalog);
					catalogs.put(goods, catalog);
					goodsList.add(goods);
					break;
				}
			}
		} else {
			if (goods != catalog.getValue()) {
				goods.copyTo((Goods) catalog.getValue());
				goods = (Goods) catalog.getValue();
			}
			if (!catalog.getParent().getValue().equals(goods.getType())) {
				catalog.getParent().getChildren().remove(catalog);
				for (Catalog c : getCatalog(Catalog.TYPE_GOODS).getChildren()) {
					if (c.getValue().equals(goods.getType())) {
						catalog.setParent(c);
						c.getChildren().add(catalog);
						break;
					}
				}
			}
		}
	}

	public static void removeCatalog(Catalog catalog, Position position) {
		catalog.getParent().getChildren().remove(catalog);
		catalogs.remove(position);
		positionList.remove(position);
	}

	public static void removeCatalog(Catalog catalog, Staff staff) {
		catalog.getParent().getChildren().remove(catalog);
		catalogs.remove(staff);
		staffList.remove(staff);
	}

	public static void removeCatalog(Catalog catalog, GoodsType goodsType) {
		catalog.getParent().getChildren().remove(catalog);
		catalogs.remove(goodsType);
		goodsTypeList.remove(goodsType);
	}

	public static void removeCatalog(Catalog catalog, Goods goods) {
		catalog.getParent().getChildren().remove(catalog);
		catalogs.remove(goods);
		goodsList.remove(goods);
	}

	public static Catalog getCatalog(int type) {
		if (type == Catalog.TYPE_ROOT) {
			return root;
		} else {
			for (Catalog catalog : root.getChildren()) {
				if (catalog.getType() == type) {
					return catalog;
				}
			}
			return null;
		}
	}

	public static Catalog getCatalog(Object value) {
		return catalogs.get(value);
	}

	public static List<Position> getPositionList() {
		return positionList;
	}

	public static List<Staff> getStaffList() {
		return staffList;
	}

	public static List<GoodsType> getGoodsTypeList() {
		return goodsTypeList;
	}

	public static List<Goods> getGoodsList() {
		return goodsList;
	}
}
