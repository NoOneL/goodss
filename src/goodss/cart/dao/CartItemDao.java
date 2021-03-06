package goodss.cart.dao;

import goodss.book.domain.Book;
import goodss.cart.domain.CartItem;
import goodss.user.domain.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	private QueryRunner qr=new TxQueryRunner();
	
	//用来生成where子句
	private String toWhereSql(int len) {
		StringBuilder sb=new StringBuilder(" cartItemId in(");
		for(int i=0;i<len;i++){
			sb.append("?");
			if(i<len-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	/**
	 * 加载多个CartItem
	 * @param cartItemIds
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> loadCartItems(String cartItemIds)throws SQLException{
		//把cartItemIds转换成数组
		Object[] cartItemArray=cartItemIds.split(",");
		//生成where子句
		String whereSql=toWhereSql(cartItemArray.length);
		//生成sql子句
		String sql="select * from t_cartitem c,t_book b where c.bid=b.bid and"
				+whereSql;
		//执行sql
		return toCartItemList(qr.query(sql, new MapListHandler(),cartItemArray));
	}

	/**
	 * 按id查询
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException {
		String sql = "select * from t_cartItem c, t_book b where c.bid=b.bid and c.cartItemId=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(), cartItemId);
		return toCartItem(map);
	}
	
	/**
	 * 批量删除
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds)throws SQLException{
		/*
		 * 需要先把cartItemIds转换成数组
		 * 1. 把cartItemIds转换成一个where子句
		 * 2. 与delete from 连接在一起，然后执行之
		 */
		Object[] cartItemIdArray=cartItemIds.split(",");
		String whereSql=toWhereSql(cartItemIdArray.length);
		String sql="delete from t_cartitem where " +whereSql;
		qr.update(sql,cartItemIdArray);
	}
	
	
	/**
	 * 查询某个用户图书的购物车条目是否存在
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem finByUidAndBid(String uid,String bid)throws SQLException{
		String sql="select * from t_cartitem where uid=? and bid=?";
		Map<String, Object>map=qr.query(sql, new MapHandler(),uid,bid);
		CartItem cartItem=toCartItem(map);
		return cartItem;
	}
	
	/**
	 * 修改指定条目的数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId,int quantity)
		throws SQLException{
		String sql="update t_cartitem set quantity=? where cartItemId=?";
		qr.update(sql,quantity,cartItemId);
	}
	
	/**
	 * 添加条目
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem)throws SQLException{
		String sql="insert into t_cartitem(cartItemId,quantity,bid,uid)"+
				"values(?,?,?,?)";
		Object[] params={cartItem.getCartItemId(),cartItem.getQuantity(),
				cartItem.getBook().getBid(),cartItem.getUser().getUid()};
		qr.update(sql,params);
		
	}
	
	/**
	 * 通过用户查询购物车条目
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid)throws SQLException{
		String sql="select *from t_cartitem c,t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String, Object>>mapList=qr.query(sql, new MapListHandler(),uid);
		return toCartItemList(mapList);
	}

	/**
	 * 把多个Map<List<Map>映射成多个CartItem(List<CartItem>)
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList) {
		List<CartItem> cartItemList=new ArrayList<CartItem>();
		for(Map<String,Object>map:mapList){
			CartItem cartItem=toCartItem(map);
			cartItemList.add(cartItem);
		}
		return cartItemList;
	}

	/**
	 * 把一个Map映射成一个cartitem
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String, Object> map) {
		if(map==null || map.size()==0)return null;
		CartItem cartItem=CommonUtils.toBean(map, CartItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		User user=CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
}
