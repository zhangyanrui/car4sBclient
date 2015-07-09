//package cn.car4s.app.util;
//
//import cn.j.guang.DailyNew;
//import cn.j.guang.entity.BaseEntity;
//import cn.j.guang.entity.CollectionEntity;
//import cn.j.guang.entity.CollectionMixEntity;
//import cn.j.guang.entity.StartConfigRedCornerEntity;
//import cn.j.guang.ui.util.LogUtil;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;
//import com.db4o.constraints.UniqueFieldValueConstraintViolationException;
//import com.db4o.ext.DatabaseClosedException;
//import com.db4o.ext.DatabaseReadOnlyException;
//import com.db4o.ext.Db4oIOException;
//import com.db4o.query.Predicate;
//import com.db4o.query.Query;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA. User: liuyu Date: 13-6-13 Time: 下午4:31 To change
// * this template use File | Settings | File Templates.
// */
//public class Db4oDao {
//
//    /**
//     * 单条插入
//     *
//     * @param baseEntity data
//     * @param tableClass table
//     */
//    public static void save(BaseEntity baseEntity, Class tableClass) {
//        long identity = getIdentity(tableClass);
//        baseEntity.identity = identity + 1;
//        DailyNew.getDb().store(baseEntity);
//        commit();
//    }
//
//    public static <T> void save(T t) {
//        DailyNew.getDb().store(t);
//        commit();
//    }
//
//    public static <T> void update(T t) {
//        DailyNew.getDb().store(t);
//        commit();
//    }
//
//    public static <T> void delete(Class<T> tableClass) {
//        Query query = DailyNew.getDb().query();
//        query.constrain(tableClass);
//        ObjectSet<T> set = query.execute();
//        while (set.hasNext()) {
//            DailyNew.getDb().delete(set.next());
//        }
//        commit();
//    }
//
//    public static <T> void delete(T t) {
//        ObjectContainer db = DailyNew.getDb();
//        ObjectSet<T> set = db.queryByExample(t);
//        LogUtil.e("delete ", "begin ");
//        while (set.hasNext()) {
//            LogUtil.e("delete ", "query get ");
//            db.delete(set.next());
//        }
//        commit();
//    }
//
//    public static boolean commit() {
//        boolean result = false;
//        try {
//            DailyNew.getDb().commit();
//            result = true;
//        } catch (UniqueFieldValueConstraintViolationException e) {
//            DailyNew.getDb().rollback();
////            Log.d("tg", "Db4oDao commit appear UniqueFieldValueConstraintViolationException : " + e.getMessage());
//            // e.printStackTrace();
//        } catch (Db4oIOException e) {
//            e.printStackTrace();
//        } catch (DatabaseClosedException e) {
//            e.printStackTrace();
//        } catch (DatabaseReadOnlyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 批量插入
//     *
//     * @param baseEntityList data
//     * @param tableClass     table
//     */
//    public static void saveAll(List<BaseEntity> baseEntityList, Class tableClass) {
//        long identity = getIdentity(tableClass);
//        for (int i = 0; i < baseEntityList.size(); i++) {
//            BaseEntity baseEntity = baseEntityList.get(i);
//            baseEntity.identity = identity + i;
//            DailyNew.getDb().store(baseEntity);
//        }
//        commit();
//    }
//
//    public static <E> void saveAll(List<E> datas) {
//        int size = datas.size();
//        for (int i = 0; i < size; i++) {
//            E e = datas.get(i);
//            DailyNew.getDb().store(e);
//            if ((i != 0) && (i % 10 == 0)) {
////                Log.d("tg", "Db4oDao save 10 datas..");
//                commit();
//            }
//        }
//        commit();
//    }
//
//    private static long getIdentity(Class tableClass) {
//        if (GetLogsCount(tableClass) == 0)
//            return 0;
//        Query query = DailyNew.getDb().query();
//        query.constrain(tableClass);
//        query.descend("identity").orderDescending();
//        ObjectSet<BaseEntity> objectSet = query.execute();
//        return objectSet == null ? 0 : objectSet.get(0).identity;
//    }
//
//    // 返回所有记录的集合
//    public static <T> List<T> fetchAllRows(Class<T> c) {
//        return DailyNew.getDb().query(c);
//    }
//
//    // 查询记录条数
//    public static int GetLogsCount(Class c) {
//        List logs = fetchAllRows(c);
//        return logs == null ? 0 : logs.size();
//    }
//
//    public static <T> List<T> selectBySome(String key[], Object values[], Class tableClass) {
//        Query query = DailyNew.getDb().query();
//        query.constrain(tableClass);
//        for (int i = 0; i < key.length; i++)
//            query.descend(key[i]).constrain(values[i]);
//        List<T> list = query.execute();
//        return list;
//    }
//
//    public static <T> List<T> selectBySome(String key, Object values, Class tableClass) {
//        Query query = DailyNew.getDb().query();
//        query.constrain(tableClass);
//        query.descend(key).constrain(values);
//        List<T> list = query.execute();
//        return list;
//    }
//
//    public static <T> ArrayList<T> queryAllSortBy(T t, boolean isDesc, String sort) {
//        Query query = DailyNew.getDb().query();
//        query.constrain(t);
//        if (isDesc) {
//            query.descend(sort).orderDescending();
//        } else {
//            query.descend(sort).orderAscending();
//        }
//        return new ArrayList<T>((ObjectSet<T>) query.execute());
//    }
//
//
//    public static ArrayList<CollectionEntity> queryLimit(int page, int count) {
//        ArrayList<CollectionEntity> _list = new ArrayList<CollectionEntity>(DailyNew.getDb().query(new Predicate<CollectionEntity>(CollectionEntity.class) {
//            @Override
//            public boolean match(CollectionEntity arg0) {
//                return true;
//            }
//        }));
//        ArrayList<CollectionEntity> list = new ArrayList<CollectionEntity>(_list);
//        Collections.sort(list, new Comparator<CollectionEntity>() {
//            @Override
//            public int compare(CollectionEntity lhs, CollectionEntity rhs) {
//                return (int) (rhs.date - lhs.date);
//            }
//
//            @Override
//            public boolean equals(Object object) {
//                return true;
//            }
//        });
//
//        if (list.size() > count * page) {
//            return new ArrayList<CollectionEntity>(list.subList(page * count, (list.size() - count * (page + 1) >= 0 ? count * (page + 1) : list.size())));
//        } else {
//            return new ArrayList<CollectionEntity>();
//        }
//    }
//
//    public static ArrayList<CollectionMixEntity> queryMixLimit(int page, int count) {
//        ArrayList<CollectionMixEntity> _list = new ArrayList<CollectionMixEntity>(DailyNew.getDb().query(new Predicate<CollectionMixEntity>(CollectionMixEntity.class) {
//            @Override
//            public boolean match(CollectionMixEntity arg0) {
//                return true;
//            }
//        }));
//        ArrayList<CollectionMixEntity> list = new ArrayList<CollectionMixEntity>(_list);
//        //java.lang.IllegalArgumentException: Comparison method violates its general contract!
//        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//        Collections.sort(list, new Comparator<CollectionMixEntity>() {
//            @Override
//            public int compare(CollectionMixEntity lhs, CollectionMixEntity rhs) {
//                if (rhs.date - lhs.date > 0) {
//                    return 1;
//                } else if (rhs.date - lhs.date < 0) {
//                    return -1;
//                } else {
//                    return 0;
//                }
////                return (int) (rhs.date - lhs.date);
//            }
//
//            @Override
//            public boolean equals(Object object) {
//                return true;
//            }
//        });
//
//        if (list.size() > count * page) {
//            return new ArrayList<CollectionMixEntity>(list.subList(page * count, (list.size() - count * (page + 1) >= 0 ? count * (page + 1) : list.size())));
//        } else {
//            return new ArrayList<CollectionMixEntity>();
//        }
//    }
//
//    public static <T> ArrayList<T> queryConstrain(T obj) {
//        return new ArrayList<T>((ObjectSet<T>) DailyNew.getDb().queryByExample(obj));
//    }
//
//    /**
//     * 查询
//     * 过期的红点
//     *
//     * @return
//     */
//    public static ArrayList<StartConfigRedCornerEntity> queryRedCornerIsValid() {
//        final long time = System.currentTimeMillis();
////        LogUtil.e("----ctime", "" + time);
//        ArrayList<StartConfigRedCornerEntity> _list = new ArrayList<StartConfigRedCornerEntity>(DailyNew.getDb().query(new Predicate<StartConfigRedCornerEntity>(StartConfigRedCornerEntity.class) {
//            @Override
//            public boolean match(StartConfigRedCornerEntity bean) {
//                return (bean.expiretTime < time);
//            }
//        }));
////        LogUtil.e("----queryRedCornerIsValid", "" + _list.size());
//        return _list;
//    }
//
//    /**
//     * 获取有效的红点
//     *
//     * @return
//     */
//    public static ArrayList<StartConfigRedCornerEntity> queryRedCornerShow() {
//        final long time = System.currentTimeMillis();
//        ArrayList<StartConfigRedCornerEntity> _list = new ArrayList<StartConfigRedCornerEntity>(DailyNew.getDb().query(new Predicate<StartConfigRedCornerEntity>(StartConfigRedCornerEntity.class) {
//            @Override
//            public boolean match(StartConfigRedCornerEntity bean) {
//                if (bean.expiretTime >= time && bean.isClicked == false) {
//                    return true;
//                }
//                return false;
//            }
//        }));
////        LogUtil.e("----queryRedCornerShow", "" + _list.size());
//        return _list;
//    }
//
//
//
//    public static ArrayList<CollectionMixEntity> queryMixLimit(int page, int count, final int type) {//type 0,普通商品 1,shop  2说说
//        ArrayList<CollectionMixEntity> _list = new ArrayList<CollectionMixEntity>(DailyNew.getDb().query(new Predicate<CollectionMixEntity>(CollectionMixEntity.class) {
//            @Override
//            public boolean match(CollectionMixEntity bean) {
//                if (type == 0) {
//                    return CollectionMixEntity.TYPE_GOOD.equals(bean.typeId);
//                } else if (type == 1) {
//                    return CollectionMixEntity.TYPE_SHOP.equals(bean.typeId);
//                } else {
//                    return (!CollectionMixEntity.TYPE_GOOD.equals(bean.typeId)) && !(CollectionMixEntity.TYPE_SHOP.equals(bean.typeId));
//                }
////                return true;
//            }
//        }));
//        ArrayList<CollectionMixEntity> list = new ArrayList<CollectionMixEntity>(_list);
//        //java.lang.IllegalArgumentException: Comparison method violates its general contract!
//        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//        Collections.sort(list, new Comparator<CollectionMixEntity>() {
//            @Override
//            public int compare(CollectionMixEntity lhs, CollectionMixEntity rhs) {
//                if (rhs.date - lhs.date > 0) {
//                    return 1;
//                } else if (rhs.date - lhs.date < 0) {
//                    return -1;
//                } else {
//                    return 0;
//                }
////                return (int) (rhs.date - lhs.date);
//            }
//
//            @Override
//            public boolean equals(Object object) {
//                return true;
//            }
//        });
//
//        if (list.size() > count * page) {
//            return new ArrayList<CollectionMixEntity>(list.subList(page * count, (list.size() - count * (page + 1) >= 0 ? count * (page + 1) : list.size())));
//        } else {
//            return new ArrayList<CollectionMixEntity>();
//        }
//    }
//}
