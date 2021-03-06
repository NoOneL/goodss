package goodss.order.domain;

import java.util.List;

import pager.Expression;

import goodss.user.domain.User;

public class Order {
	private String oid;//主键
	private String ordertime;//下单时间
	private double total;//总计
	private int status;//订单状态
	private String address;//收货地址
	private User owner;//订单的所有者
	
	private List<OrderItem> orderItemList;
	
	//private List<OrderItem>orderItemList;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public List<OrderItem>getOrderItems(){
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		// TODO Auto-generated method stub
		this.orderItemList=orderItemList;
	}
	public List<OrderItem> getOrderItemList() {
		// TODO Auto-generated method stub
		return orderItemList;
	}
	
	
}
