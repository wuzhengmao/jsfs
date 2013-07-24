package org.mingy.jsfs.ui.model;

import java.util.HashMap;
import java.util.Map;

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
		loadStaffs(staffCatalog);
		loadGoods(goodsCatalog);
		inited = true;
	}

	private static void loadStaffs(Catalog parent) {
		for (Position position : staffFacade.getPositions()) {
			Catalog catalog = new Catalog(position);
			catalog.setParent(parent);
			parent.getChildren().add(catalog);
		}
		for (Staff staff : staffFacade.getStaffs()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(staff.getPosition())) {
					Catalog child = new Catalog(staff);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(staff, child);
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
		}
		for (Goods goods : goodsFacade.getGoods()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(goods.getType())) {
					Catalog child = new Catalog(goods);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(goods, child);
					break;
				}
			}
		}
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
}
